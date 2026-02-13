package matt.pass.mojaryba.domain.email;

import jakarta.transaction.Transactional;
import matt.pass.mojaryba.domain.email.dto.EmailSettingsForm;
import matt.pass.mojaryba.domain.email.dto.EmailSettingsWrapper;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class EmailSettingsService {

    private final EmailSettingsRepository emailSettingsRepository;

    public EmailSettingsService(EmailSettingsRepository emailSettingsRepository) {
        this.emailSettingsRepository = emailSettingsRepository;
    }

    public EmailSettingsWrapper findAllEmailSettingsFormWrap() {
        final List<EmailSettingsForm> settingsForm = emailSettingsRepository.findAll().stream()
                .map(EmailSettingsMapper::map)
                .toList();

        final EmailSettingsWrapper emailSettingsWrapper = new EmailSettingsWrapper();
        emailSettingsWrapper.setEmailSettings(settingsForm);
        return emailSettingsWrapper;
    }

    @Transactional
    public void saveEmailSettings(EmailSettingsWrapper settings) {
        settings.getEmailSettings().forEach(this::updateEmailSetting);
    }

    private void updateEmailSetting(EmailSettingsForm settingsForm) {
        final EmailSettings.EmailType emailType = settingsForm.getEmailType();
        emailSettingsRepository.findByEmailType(emailType).orElseThrow(
                        () -> new IllegalArgumentException("Nie znaleziono mailingu dla typu: " + emailType))
                .setEnabled(settingsForm.isEnabled());
    }

    public boolean verifyMailingStatus(EmailSettings.EmailType emailType) {
        return emailSettingsRepository.findByEmailType(emailType).orElseThrow().isEnabled();
    }
}
