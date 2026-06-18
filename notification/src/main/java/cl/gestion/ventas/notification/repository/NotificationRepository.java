package cl.gestion.ventas.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import cl.gestion.ventas.notification.models.Notification;


public interface NotificationRepository extends JpaRepository<Notification, Long>{

}
