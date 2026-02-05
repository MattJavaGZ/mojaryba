package matt.pass.mojaryba.infrastructure.files;

import matt.pass.mojaryba.domain.photos.FishPhotos;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
@ExtendWith(MockitoExtension.class)
class ImageServiceTest {


    private ImageService imageService;

    @BeforeEach
    void setUp() {
    imageService = new ImageService("uploads/photos/", "uploads/miniatures/");
    }

    @Test
    public void shouldSaveImage() throws IOException {
        //given
        File file = new File("uploads/photos/220.jpeg");
        final FileInputStream fileInputStream = new FileInputStream(file);
        final MockMultipartFile imageToSave = new MockMultipartFile("file", file.getName(), "image/jpeg", fileInputStream);
        //when
        imageService.saveImage(imageToSave);
        //then
        File savedFile = new File("uploads/photos/2200.jpeg");
        Assertions.assertThat(savedFile).exists();
        Files.deleteIfExists(savedFile.toPath());
    }

    @Test
    public void shouldDeleteImage() throws IOException {
        //given
        File file = new File("uploads/photos/220.jpeg");
        final FileInputStream fileInputStream = new FileInputStream(file);
        Files.copy(fileInputStream, Path.of("uploads/photos/tempFile.jpeg"), StandardCopyOption.REPLACE_EXISTING);

        final FishPhotos fishPhoto = new FishPhotos();
        fishPhoto.setPhoto("tempFile.jpeg");
        //when
        imageService.deleteImages(List.of(fishPhoto));
        //then
        Assertions.assertThat(Path.of("uploads/photos/tempFile.jpeg")).doesNotExist();

    }

}