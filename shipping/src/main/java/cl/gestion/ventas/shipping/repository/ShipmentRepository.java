package cl.gestion.ventas.shipping.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.gestion.ventas.shipping.model.Shipment;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long>{
    Optional<Shipment> findByTrackingNumber(String trknum);

    boolean existsByTrackingNumber(String trknum);

    void deleteByTrackingNumber(String number);
}
