package cl.gestion.ventas.category.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.gestion.ventas.category.dto.CategoryResponseDTO;
import cl.gestion.ventas.category.mapper.CategoryMapper;
import cl.gestion.ventas.category.model.Category;
import cl.gestion.ventas.category.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CategoryService {

    @Autowired
    private CategoryRepository repo;

    @Autowired
    private CategoryMapper mapper;

    public List<Category> buscarCategorias() {
        log.info("Consultando categorias..");
        return repo.findAll();
    }

    public CategoryResponseDTO buscarCategoriaPorId(Long id) {
        log.info("Consultando categoria por id: {}", id);
        Category category = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontro esa categoria."));
        return mapper.toResponse(category);
    }

    public void eliminarCategoria(Long id) {
        log.info("Eliminando categoria con la id: {}", id);

        if (!repo.existsById(id)) {
            throw new NoSuchElementException("No se encontro la categoria con esa id.");

        }
        repo.deleteById(id);
    }
}
