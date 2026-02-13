package matt.pass.mojaryba.domain.email;

import matt.pass.mojaryba.domain.email.dto.EmailSettingsForm;

public class EmailSettingsMapper {

    public static EmailSettingsForm map(EmailSettings emailSettings) {

        return new EmailSettingsForm(emailSettings.getEmailType(), emailSettings.isEnabled());
    }
}
