package matt.pass.mojaryba.domain.comment;

import jakarta.persistence.*;
import matt.pass.mojaryba.domain.fish.Fish;
import matt.pass.mojaryba.domain.user.User;

import java.time.LocalDateTime;

@Entity
public class Comment implements Comparable<Comment> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "fish_id")
    private Fish fish;
    private String content;
    private LocalDateTime dateAdded;

    public Comment(User user, Fish fish, String content) {
        this.user = user;
        this.fish = fish;
        this.content = content;
        this.dateAdded = LocalDateTime.now();
    }

    public Comment() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Fish getFish() {
        return fish;
    }

    public void setFish(Fish fish) {
        this.fish = fish;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(LocalDateTime dateAdded) {
        this.dateAdded = dateAdded;
    }

    @Override
    public int compareTo(Comment o) {
        return -dateAdded.compareTo(o.dateAdded);
    }
}
