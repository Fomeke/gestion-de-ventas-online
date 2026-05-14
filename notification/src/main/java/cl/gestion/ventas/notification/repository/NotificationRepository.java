package cl.gestion.ventas.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.gestion.ventas.notification.models.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long>{

}
