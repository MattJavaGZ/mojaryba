package matt.pass.mojaryba.domain.email.dto;

import java.util.ArrayList;
import java.util.List;


public class EmailSettingsWrapper {

    private List<EmailSettingsForm> emailSettings = new ArrayList<>();

    public List<EmailSettingsForm> getEmailSettings() {
        return emailSettings;
    }

    public void setEmailSettings(List<EmailSettingsForm> emailSettings) {
        this.emailSettings = emailSettings;
    }
}
