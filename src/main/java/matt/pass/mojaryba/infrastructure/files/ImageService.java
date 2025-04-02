package matt.pass.mojaryba.infrastructure.files;

import matt.pass.mojaryba.domain.photos.FishPhotos;
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

@Service
public class ImageService {

    private final String imageStorageLocation;

    public ImageService(@Value("${app.storage.location}") String imageStorageLocation) {
        this.imageStorageLocation = imageStorageLocation;
        final Path photoStoragePath = Path.of(imageStorageLocation);
        createStorageDirectories(photoStoragePath);
    }

    private void createStorageDirectories(Path photoStoragePath) {
        if (Files.notExists(photoStoragePath)) {
            try {
                Files.createDirectories(photoStoragePath);
            } catch (IOException e) {
                throw new UncheckedIOException("Błąd tworzenia katalogu", e);
            }
        }
    }

    public String saveImage(MultipartFile file) {
        Path filePath = createFilePath(file, imageStorageLocation);
        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return filePath.getFileName().toString();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    public void deleteImages(List<FishPhotos> images) {
        for (FishPhotos image : images) {
            final Path imagePath = Path.of(imageStorageLocation, image.getPhoto());
            try {
                Files.deleteIfExists(imagePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
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
        }while (Files.exists(filePath));
        return filePath;
    }


}
