package cl.gestion.ventas.category.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.gestion.ventas.category.dto.CategoryResponseDTO;
import cl.gestion.ventas.category.model.Category;
import cl.gestion.ventas.category.service.CategoryService;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/v1/category")
public class CategoryController {

    @Autowired
    private CategoryService service;

    @GetMapping
    public ResponseEntity<List<Category>> getCategoria(){
        log.info("GET api/category/buscarCategorias");
        return ResponseEntity.ok().body(service.buscarCategorias());
    }

    @GetMapping("/porid/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategoriaPorId(@PathVariable Long id){
        log.info("GET api/category/categoriaPorId/{}", id);
        return ResponseEntity.ok().body(service.buscarCategoriaPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoria(@PathVariable Long id){
        log.info("DELETE api/category/eliminarCategoria/{}", id);
        service.eliminarCategoria(id);
        return ResponseEntity.noContent().build();
    }
}
