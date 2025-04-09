package matt.pass.mojaryba.domain.type;

import jakarta.persistence.*;

@Entity
public class FishType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(name = "fish_species", nullable = false)
    private FishSpecies fishSpecies;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FishSpecies getFishSpecies() {
        return fishSpecies;
    }

    public void setFishSpecies(FishSpecies fishSpecies) {
        this.fishSpecies = fishSpecies;
    }

    public enum FishSpecies {
        PREDATOR("Drapieżnik"), WHITEFISH("Białoryb"), OTHER("Inny");

        private String name;

        FishSpecies(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
