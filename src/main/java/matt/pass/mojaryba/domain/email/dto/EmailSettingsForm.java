package matt.pass.mojaryba.domain.email.dto;

import matt.pass.mojaryba.domain.email.EmailSettings;


public class EmailSettingsForm {

    private EmailSettings.EmailType emailType;
    private boolean enabled;

    public EmailSettingsForm() {
    }

    public EmailSettingsForm(EmailSettings.EmailType emailType, boolean enabled) {
        this.emailType = emailType;
        this.enabled = enabled;
    }

    public EmailSettings.EmailType getEmailType() {
        return emailType;
    }

    public void setEmailType(EmailSettings.EmailType emailType) {
        this.emailType = emailType;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}