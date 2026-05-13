package cl.gestion.ventas.order.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.gestion.ventas.order.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

    Optional<Order> findByUserId(Long id);

    boolean existsByUserId(Long id);

    void deleteByUserId(Long id);
}
