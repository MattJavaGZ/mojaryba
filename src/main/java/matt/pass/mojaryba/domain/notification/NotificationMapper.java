package matt.pass.mojaryba.domain.notification;

import matt.pass.mojaryba.domain.notification.dto.NotificationDto;

public class NotificationMapper {


    public static NotificationDto mapNotificationToNotificationDto(Notification notification){
        return new NotificationDto(
                notification.getId(),
                notification.getDescription(),
                notification.getFish(),
                notification.isRead()
        );
    }
}
