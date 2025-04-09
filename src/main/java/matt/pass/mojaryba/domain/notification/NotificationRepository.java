package matt.pass.mojaryba.domain.notification;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NotificationRepository extends CrudRepository<Notification, Long> {
    List<Notification> findAllByUser_Email(String email);
}
