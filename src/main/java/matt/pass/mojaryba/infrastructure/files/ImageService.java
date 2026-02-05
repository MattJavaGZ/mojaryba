package matt.pass.mojaryba.infrastructure.files;

import matt.pass.mojaryba.domain.photos.FishPhotos;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Stream;

@Service
public class ImageService {

    private final String imageStorageLocation;
    private final String miniatureImageStorageLocation;

    public ImageService(@Value("${app.storage.photo-location}") String imageStorageLocation,
                        @Value("${app.storage.miniature-photo-location}") String miniatureImageStorageLocation) {
        this.imageStorageLocation = imageStorageLocation;
        this.miniatureImageStorageLocation = miniatureImageStorageLocation;

        final Path photoStoragePath = Path.of(imageStorageLocation);
        createStorageDirectories(photoStoragePath);
        final Path miniaturePhotoStoragePath = Path.of(miniatureImageStorageLocation);
        createStorageDirectories(miniaturePhotoStoragePath);
    }

    private void createStorageDirectories(Path storagePath) {
        if (Files.notExists(storagePath)) {
            try {
                Files.createDirectories(storagePath);
            } catch (IOException e) {
                throw new UncheckedIOException("Błąd tworzenia katalogu", e);
            }
        }
    }

    public String saveImage(MultipartFile file) {

        Path imagePath = createFilePath(file, imageStorageLocation);
        try {
            Files.copy(file.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
            saveMiniature(imagePath);
            return imagePath.getFileName().toString();
        } catch (IOException e) {
            throw new UncheckedIOException("Błąd zapisu obrazu", e);
        }
    }

    private void saveMiniature(Path imagePath) {
        final String fileName = imagePath.getFileName().toString();
        final Path miniatureImagePath = Path.of(miniatureImageStorageLocation, fileName);

        if (!Files.exists(miniatureImagePath)) {
            try {
                Thumbnails.of(imagePath.toFile())
                        .width(300)
                        .outputQuality(0.8)
                        .toFile(miniatureImagePath.toFile());
            } catch (IOException e) {
                throw new UncheckedIOException("Błąd zapisu miniatury", e);
            }
        }
    }

    private Path createFilePath(MultipartFile file, String imageStorageLocation) {
        final String originalFilename = file.getOriginalFilename();
        final String baseName = FilenameUtils.getBaseName(originalFilename);
        final String extension = FilenameUtils.getExtension(originalFilename);
        String completeFileName;
        int index = 0;
        Path filePath;
        do {
            completeFileName = baseName + index + "." + extension;
            filePath = Path.of(imageStorageLocation, completeFileName);
            index++;
        } while (Files.exists(filePath));
        return filePath;
    }

    public void deleteImages(List<FishPhotos> images) {
        for (FishPhotos image : images) {
            final Path imagePath = Path.of(imageStorageLocation, image.getPhoto());
            final Path miniatureImagePath = Path.of(miniatureImageStorageLocation, image.getPhoto());
            try {
                Files.deleteIfExists(imagePath);
                Files.deleteIfExists(miniatureImagePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void generateMiniaturesForCurrentPhotos() {
        Path photosDir = Path.of(imageStorageLocation);

        try (Stream<Path> files = Files.list(photosDir)) {

            files
                    .filter(Files::isRegularFile)
                    .filter(this::isImage)
                    .forEach(this::saveMiniature);

        } catch (IOException e) {
            throw new UncheckedIOException("Błąd pobrania listy zdjęć",e);
        }
    }

    private boolean isImage(Path imagePath){
        final String fileName = imagePath.getFileName().toString().toLowerCase();
        return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png") || fileName.endsWith(".gif");
    }
}
