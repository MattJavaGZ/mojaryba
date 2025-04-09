package matt.pass.mojaryba.domain.notification.dto;

import matt.pass.mojaryba.domain.fish.Fish;

public class NotificationDto {

    private Long id;
    private String description;
    private Fish fish;
    private boolean read;

    public NotificationDto(Long id, String description, Fish fish, boolean read) {
        this.id = id;
        this.description = description;
        this.fish = fish;
        this.read = read;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Fish getFish() {
        return fish;
    }

    public void setFish(Fish fish) {
        this.fish = fish;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
