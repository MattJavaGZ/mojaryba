package matt.pass.mojaryba.domain.fish;

import jakarta.persistence.*;
import matt.pass.mojaryba.domain.comment.Comment;
import matt.pass.mojaryba.domain.like.Like;
import matt.pass.mojaryba.domain.photos.FishPhotos;
import matt.pass.mojaryba.domain.rating.Rating;
import matt.pass.mojaryba.domain.type.FishType;
import matt.pass.mojaryba.domain.user.User;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
public class Fish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private LocalDateTime dateAdded;
    private String description;
    private double weight;
    private int length;
    private String fishingMethod;  //metoda połowu
    private String bait;           // przynęta
    private String fishingSpot;
    @OneToMany(mappedBy = "fish", cascade = CascadeType.REMOVE)
    private List<FishPhotos> photos = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "fish_type_id", referencedColumnName = "id")
    private FishType fishType;
    @OneToMany(mappedBy = "fish", cascade = CascadeType.REMOVE)
    private List<Rating> ratings = new ArrayList<>();
    @OneToMany(mappedBy = "fish", cascade = CascadeType.REMOVE)
    private List<Like> likes = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @OneToMany(mappedBy = "fish", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(LocalDateTime dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
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


    public FishType getFishType() {
        return fishType;
    }

    public void setFishType(FishType fishType) {
        this.fishType = fishType;
    }

    public List<FishPhotos> getPhotos() {
        return photos;
    }

    public void setPhotos(List<FishPhotos> photos) {
        this.photos = photos;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }

        Fish fish = (Fish) o;

        return id != null && id.equals(fish.id);
    }

    @Override
    public int hashCode() {
        return Hibernate.getClass(this).hashCode();
    }
}
