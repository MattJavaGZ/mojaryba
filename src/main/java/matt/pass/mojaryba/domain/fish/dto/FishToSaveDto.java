package matt.pass.mojaryba.domain.fish.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class FishToSaveDto {
    @NotBlank
    @Size(min = 3, max = 40, message = "Tytuł może zawierać minimalnie {min} znaki a maksymalnie {max} znaków")
    private String title;
    @Size(max = 1000, message = "Opis może zawierać maksymalnie {max} znaków")
    private String description;
    private Double weight;
    private Integer length;
    @Size(max = 50, message = "Nazwa metody połowu może zawierać maksymalnie 50 znaków")
    private String fishingMethod;  //metoda połowu
    @Size(max = 50, message = "Nazwa przynęty może zawierać maksymalnie 50 znaków")
    private String bait;// przynęta
    @Size(max = 100, message = "Nazwa łowiska może zawierać maksymalnie 50 znaków")
    private String fishingSpot;
    @Size(max = 6, message = "Maksymalnie 6 zdjęć")
    private List<MultipartFile> photos;
    private String fishType;

    public FishToSaveDto() {
    }

    public FishToSaveDto(String title, String description, Double weight, Integer length, String fishingMethod,
                         String bait, String fishingSpot, List<MultipartFile> photos, String fishType) {
        this.title = title;
        this.description = description;
        this.weight = weight;
        this.length = length;
        this.fishingMethod = fishingMethod;
        this.bait = bait;
        this.fishingSpot = fishingSpot;
        this.photos = photos;
        this.fishType = fishType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String getFishingMethod() {
        return fishingMethod;
    }

    public void setFishingMethod(String fishingMethod) {
        this.fishingMethod = fishingMethod;
    }

    public String getBait() {
        return bait;
    }

    public void setBait(String bait) {
        this.bait = bait;
    }

    public String getFishingSpot() {
        return fishingSpot;
    }

    public void setFishingSpot(String fishingSpot) {
        this.fishingSpot = fishingSpot;
    }

    public List<MultipartFile> getPhotos() {
        return photos;
    }

    public void setPhotos(List<MultipartFile> photos) {
        this.photos = photos;
    }

    public String getFishType() {
        return fishType;
    }

    public void setFishType(String fishType) {
        this.fishType = fishType;
    }
}
