package cl.gestion.ventas.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import cl.gestion.ventas.product.model.Product;


public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByName(String name);

    @Override
    boolean existsById(Long id);
}
