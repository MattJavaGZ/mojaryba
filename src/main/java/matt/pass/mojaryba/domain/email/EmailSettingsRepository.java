package matt.pass.mojaryba.domain.email;

import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface EmailSettingsRepository extends ListCrudRepository<EmailSettings, Long> {

    Optional<EmailSettings> findByEmailType(EmailSettings.EmailType emailType);

}
