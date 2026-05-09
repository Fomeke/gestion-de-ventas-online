package cl.gestion.ventas.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.gestion.ventas.category.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
