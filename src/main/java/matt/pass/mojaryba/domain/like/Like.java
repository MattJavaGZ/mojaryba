package matt.pass.mojaryba.domain.like;

import jakarta.persistence.*;
import matt.pass.mojaryba.domain.fish.Fish;
import matt.pass.mojaryba.domain.user.User;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "likes")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "fish_id")
    private Fish fish;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Like(Fish fish, User user) {
        this.fish = fish;
        this.user = user;
    }

    public Like() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Fish getFish() {
        return fish;
    }

    public void setFish(Fish fish) {
        this.fish = fish;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
