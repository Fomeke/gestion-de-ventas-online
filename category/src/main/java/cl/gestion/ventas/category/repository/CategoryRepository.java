package cl.gestion.ventas.category.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import cl.gestion.ventas.category.model.Category;




public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    Optional<Category>  findByName(String name);
}
