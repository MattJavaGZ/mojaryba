package matt.pass.mojaryba.domain.email;

import jakarta.persistence.*;

@Entity
@Table(name = "email_settings")
public class EmailSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "email_type", nullable = false, length = 50, unique = true)
    private EmailType emailType;
    @Column(columnDefinition = "TINYINT(1)")
    private boolean enabled;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EmailType getEmailType() {
        return emailType;
    }

    public void setEmailType(EmailType emailType) {
        this.emailType = emailType;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public enum EmailType{
        COMMENT, RATING, NEW_FISH;

    }
}
