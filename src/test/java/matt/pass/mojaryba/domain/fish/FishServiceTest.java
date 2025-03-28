package matt.pass.mojaryba.domain.fish;

import matt.pass.mojaryba.domain.fish.dto.FishDto;
import matt.pass.mojaryba.domain.fish.dto.FishToSaveDto;
import matt.pass.mojaryba.domain.photos.FishPhotoRepository;
import matt.pass.mojaryba.domain.photos.FishPhotos;
import matt.pass.mojaryba.domain.type.FishType;
import matt.pass.mojaryba.domain.type.FishTypeRepository;
import matt.pass.mojaryba.domain.user.User;
import matt.pass.mojaryba.infrastructure.files.ImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FishServiceTest {

    @Mock
    private FishRepository fishRepositoryMock;
    @Mock
    private FishTypeRepository fishTypeRepositoryMock;
    @Mock
    private FishPhotoRepository fishPhotoRepositoryMock;
    @Mock
    private ImageService imageServiceMock;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;
    @InjectMocks
    private FishService fishService;

    private List<Fish> fishes = new ArrayList<>();

    @BeforeEach
    public void testData() {
        final FishType fishTypeKarp = new FishType();
        fishTypeKarp.setName("Karp");
        Fish fish1 = new Fish();
        fish1.setId(1L);
        fish1.setFishType(fishTypeKarp);
        fish1.setDescription("Karp złowiony na stawie");
        fish1.setTitle("Testowy opis");
        fish1.setFishingMethod("");
        fish1.setFishingSpot("");
        fish1.setBait("");
        fish1.setDateAdded(LocalDateTime.of(2025, 3, 10, 1, 10, 45));

        Fish fish2 = new Fish();
        fish2.setId(2L);
        fish2.setFishType(fishTypeKarp);
        fish2.setDescription("");
        fish2.setTitle("");
        fish2.setFishingMethod("");
        fish2.setFishingSpot("");
        fish2.setBait("");
        fish2.setDateAdded(LocalDateTime.of(2025, 4, 13, 23, 59, 45));

        final Fish fish3 = new Fish();
        fish3.setId(3L);
        final FishType fishTypeSum = new FishType();
        fishTypeSum.setName("Sum");
        fish3.setFishType(fishTypeSum);
        fish3.setDescription("");
        fish3.setTitle("");
        fish3.setFishingMethod("");
        fish3.setFishingSpot("");
        fish3.setBait("");
        fish3.setDateAdded(LocalDateTime.of(2025, 3, 13, 23, 59, 45));

        fishes.add(fish1);
        fishes.add(fish2);
        fishes.add(fish3);
    }

    @Test
    public void shouldReturnFish1AndFish3ForUserAndDate() {
        //given
        when(fishRepositoryMock.findAllByUser_Email(ArgumentMatchers.anyString())).thenReturn(fishes);
        //when
        final List<FishDto> fishesByUserAndDate = fishService.findFishesByUserAndDate("user@onet.pl", LocalDate.of(2025, 3, 10),
                LocalDate.of(2025, 3, 13));
        //then
        assertThat(fishesByUserAndDate.size()).isEqualTo(2);
        assertThat(fishesByUserAndDate.get(0).getId()).isEqualTo(3L);
        assertThat(fishesByUserAndDate.get(1).getId()).isEqualTo(1L);
    }

    @Test
    public void shouldThrowExceptionForNotFoundUserToSave(){
        //given
        when(fishRepositoryMock.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> fishService.findByIdToSave(1L))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void shouldCorrectSaveTwoPhotosNameToDatabaseForNewFish() throws IOException {
        //given
        File file = new File("uploads/photos/220.jpeg");
        final FileInputStream fileInputStream = new FileInputStream(file);
        final MockMultipartFile image1 = new MockMultipartFile("file", file.getName(), "image/jpeg", fileInputStream);
       
        File file2 = new File("uploads/photos/330.jpeg");
        final FileInputStream fileInputStream2 = new FileInputStream(file2);
        final MockMultipartFile image2 = new MockMultipartFile("file", file.getName(), "image/jpeg", fileInputStream2);

        final List<MultipartFile> images = List.of(image1, image2);

        final FishToSaveDto fishToSave = new FishToSaveDto();
        fishToSave.setTitle("karp");
        fishToSave.setDescription("karp");
        fishToSave.setWeight(1);
        fishToSave.setLength(1);
        fishToSave.setFishingSpot("karp");
        fishToSave.setBait("karp");
        fishToSave.setFishingMethod("karp");
        fishToSave.setFishType("Karp");
        fishToSave.setPhotos(images);

        final User user = new User();

        when(fishTypeRepositoryMock.findByName(ArgumentMatchers.anyString())).thenReturn(Optional.of(new FishType()));
        when(fishRepositoryMock.save(ArgumentMatchers.any())).thenReturn(fishes.getFirst());
        when(imageServiceMock.saveImage(image1)).thenReturn(image1.getOriginalFilename());
        when(imageServiceMock.saveImage(image2)).thenReturn(image2.getOriginalFilename());
        final ArgumentCaptor<FishPhotos> fishPhotosArgumentCaptor = ArgumentCaptor.forClass(FishPhotos.class);

        //when
        fishService.createFishFromForm(fishToSave, user);

        //then
        verify(fishPhotoRepositoryMock, times(2)).save(fishPhotosArgumentCaptor.capture());
        final List<FishPhotos> allValues = fishPhotosArgumentCaptor.getAllValues();
        assertThat(allValues.size()).isEqualTo(2);
        assertThat(allValues.getFirst().getFish().getTitle()).isEqualTo("Testowy opis");
        assertThat(allValues.getFirst().getPhoto()).isEqualTo("220.jpeg");
    }

    @Test
    public void shouldReturnIsFishAuthor() {
        //given
        final Fish fish = fishes.getFirst();
        final User user = new User();
        user.setEmail("test@moja-ryba.pl");
        fish.setUser(user);

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@moja-ryba.pl");
        when(fishRepositoryMock.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(fish));

        //when
        //then
        final boolean isAuthorOdAdmin = fishService.verificationFishAuthorOrAdmin(1L);
        assertThat(isAuthorOdAdmin).isTrue();
    }

    @Test
    public void shouldReturnIsAdmin() {
        //given
        final Fish fish = fishes.getFirst();
        final User user = new User();
        user.setEmail("test2@moja-ryba.pl");
        fish.setUser(user);

        Collection<GrantedAuthority> roles = Set.of(
                new SimpleGrantedAuthority("ROLE_ADMIN")
        );

        when(fishRepositoryMock.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(fish));
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@moja-ryba.pl");
        doReturn(roles).when(authentication).getAuthorities();
        //when
        //then
        final boolean isAuthorOrAdmin = fishService.verificationFishAuthorOrAdmin(1L);
        assertThat(isAuthorOrAdmin).isTrue();
    }
}