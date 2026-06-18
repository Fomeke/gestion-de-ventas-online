package cl.gestion.ventas.cart.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import cl.gestion.ventas.cart.model.Cart;


public interface CartRepository extends JpaRepository<Cart, Long> {
    
    Optional<Cart> findByUserId(Long id);

    boolean existsByUserId(Long id);

    void deleteByUserId(Long id);
}
