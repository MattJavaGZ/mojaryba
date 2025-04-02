package matt.pass.mojaryba.domain.fish;

import matt.pass.mojaryba.domain.comment.Comment;
import matt.pass.mojaryba.domain.comment.CommentRepository;
import matt.pass.mojaryba.domain.fish.dto.FishDto;
import matt.pass.mojaryba.domain.fish.dto.FishToSaveDto;
import matt.pass.mojaryba.domain.like.Like;
import matt.pass.mojaryba.domain.like.LikeRepository;
import matt.pass.mojaryba.domain.photos.FishPhotoRepository;
import matt.pass.mojaryba.domain.photos.FishPhotos;
import matt.pass.mojaryba.domain.rating.Rating;
import matt.pass.mojaryba.domain.rating.RatingRepository;
import matt.pass.mojaryba.domain.type.FishType;
import matt.pass.mojaryba.domain.type.FishTypeRepository;
import matt.pass.mojaryba.domain.user.User;
import matt.pass.mojaryba.infrastructure.files.ImageService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class FishService {
    private final static int PAGE_SIZE = 15;
    private final FishRepository fishRepository;
    private final FishTypeRepository fishTypeRepository;
    private final ImageService imageService;
    private final FishPhotoRepository fishPhotoRepository;
    private final CommentRepository commentRepository;
    private final RatingRepository ratingRepository;
    private final LikeRepository likeRepository;

    public FishService(FishRepository fishRepository, FishTypeRepository fishTypeRepository, ImageService imageService,
                       FishPhotoRepository fishPhotoRepository, CommentRepository commentRepository,
                       RatingRepository ratingRepository, LikeRepository likeRepository) {
        this.fishRepository = fishRepository;
        this.fishTypeRepository = fishTypeRepository;
        this.imageService = imageService;
        this.fishPhotoRepository = fishPhotoRepository;
        this.commentRepository = commentRepository;
        this.ratingRepository = ratingRepository;
        this.likeRepository = likeRepository;
    }

    public List<FishDto> findAllFishes(int page) {
        Pageable pageable = getPageable(page);
        return fishRepository.findAll(pageable)
                .stream()
                .map(FishMapper::mapFishToFishDto)
//                .sorted()
                .toList();
    }

    public int totalPagesAllFishes(int page) {
        Pageable pageable = getPageable(page);
        return fishRepository.findAll(pageable).getTotalPages();
    }

    private static Pageable getPageable(int page) {
        return PageRequest.of(page, PAGE_SIZE, Sort.by("dateAdded").descending());
    }

    public List<FishDto> findFishesByType(String typeName, int page) {
        Pageable pageable = getPageable(page);
        return fishRepository.findAllByFishType_Name(typeName, pageable).stream()
                .map(FishMapper::mapFishToFishDto)
                .toList();
    }

    public int totalPagesFishesByType(String typeName, int page) {
        Pageable pageable = getPageable(page);
        return fishRepository.findAllByFishType_Name(typeName, pageable).getTotalPages();
    }

    public Optional<FishDto> findById(long id) {
        return fishRepository.findById(id)
                .map(FishMapper::mapFishToFishDto);
    }

    public List<FishDto> findAllByUserEmail(String userEmail) {
        return fishRepository.findAllByUser_Email(userEmail).stream()
                .map(FishMapper::mapFishToFishDto)
                .sorted()
                .toList();
    }

    public List<FishDto> findAllByUserNick(String nick) {
        return fishRepository.findAllByUser_Nick(nick).stream()
                .map(FishMapper::mapFishToFishDto)
                .sorted()
                .toList();
    }

    public List<FishDto> findAllCommentedFishesByUser(String userEmail) {
        return commentRepository.findAllByUser_Email(userEmail).stream()
                .map(Comment::getFish)
                .distinct()
                .map(FishMapper::mapFishToFishDto)
                .sorted()
                .toList();
    }

    public List<FishDto> findAllRatedFishesByUser(String userEmail) {
        return ratingRepository.findAllByUser_Email(userEmail).stream()
                .map(Rating::getFish)
                .map(FishMapper::mapFishToFishDto)
                .sorted()
                .toList();
    }

    public List<FishDto> findAllLikedFishesByUser(String userEmail) {
        return likeRepository.findAllByUser_Email(userEmail).stream()
                .map(Like::getFish)
                .map(FishMapper::mapFishToFishDto)
                .sorted()
                .toList();
    }

    public List<FishDto> findFishesByTypeAndUser(String fishTypeName, String userEmail) {
        return fishRepository.findAllByFishType_NameAndUser_Email(fishTypeName, userEmail).stream()
                .map(FishMapper::mapFishToFishDto)
                .sorted()
                .toList();
    }

    public List<FishDto> findFishesByUserAndDate(String userEmail, LocalDate start, LocalDate end) {
        return fishRepository.findAllByUser_Email(userEmail).stream()
                .filter(fish -> fish.getDateAdded().isAfter(
                        start.atStartOfDay()) && fish.getDateAdded().isBefore(end.atStartOfDay().plusHours(24)))
                .map(FishMapper::mapFishToFishDto)
                .sorted()
                .toList();
    }

    public FishToSaveDto findByIdToSave(long id) {
        return fishRepository.findById(id)
                .map(FishMapper::mapFishToFishToSave)
                .orElseThrow();
    }

    public long createFishFromForm(FishToSaveDto fishToSaveDto, User user) {
        final FishType fishType = fishTypeRepository.findByName(fishToSaveDto.getFishType()).orElseThrow();
        Fish fish = FishMapper.mapFishToSaveToFish(fishToSaveDto, fishType, user);

        final Fish savedFish = fishRepository.save(fish);
        savePhotos(fishToSaveDto, savedFish);

        return savedFish.getId();
    }

    private void savePhotos(FishToSaveDto fishToSaveDto, Fish savedFish) {
        final List<MultipartFile> photos = fishToSaveDto.getPhotos();
        for (MultipartFile photo : photos) {
            if (!photo.isEmpty()) {
                final String photoName = imageService.saveImage(photo);
                final FishPhotos fishPhotosToSave = new FishPhotos();
                fishPhotosToSave.setFish(savedFish);
                fishPhotosToSave.setPhoto(photoName);
                fishPhotoRepository.save(fishPhotosToSave);
            }

        }
    }

    @Transactional
    public void editFish(FishToSaveDto fishToSave, long fishToEditId) {
        final Fish fishToEdit = fishRepository.findById(fishToEditId).orElseThrow();
        fishToEdit.setTitle(fishToSave.getTitle());
        fishToEdit.setDescription(fishToSave.getDescription());
        fishToEdit.setWeight(fishToSave.getWeight());
        fishToEdit.setLength(fishToSave.getLength());
        fishToEdit.setFishingMethod(fishToSave.getFishingMethod());
        fishToEdit.setBait(fishToSave.getBait());
        fishToEdit.setFishingSpot(fishToSave.getFishingSpot());
        if (!fishToEdit.getFishType().getName().equals(fishToSave.getFishType())) {
            final FishType fishType = fishTypeRepository.findByName(fishToSave.getFishType()).orElseThrow();
            fishToEdit.setFishType(fishType);
        }
        if (!fishToSave.getPhotos().isEmpty()) {
            savePhotos(fishToSave, fishToEdit);
        }
    }

    public void deleteFishById(long id) {
        final List<FishPhotos> photosToDelete = fishPhotoRepository.findAllByFish_Id(id);
        imageService.deleteImages(photosToDelete);
        fishRepository.deleteById(id);
    }

    public boolean verificationFishAuthorOrAdmin(long fishId) {
        final String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        final Fish fish = fishRepository.findById(fishId).orElseThrow();

        boolean isAuthor = fish.getUser() != null && fish.getUser().getEmail().equals(userEmail);

        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        return isAdmin || isAuthor;
    }
}
