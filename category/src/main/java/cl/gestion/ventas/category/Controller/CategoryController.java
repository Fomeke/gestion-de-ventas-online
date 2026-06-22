package cl.gestion.ventas.category.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.gestion.ventas.category.dto.CategoryRequest;
import cl.gestion.ventas.category.dto.CategoryResponseDTO;
import cl.gestion.ventas.category.dto.ErrorApiResponse;
import cl.gestion.ventas.category.model.Category;
import cl.gestion.ventas.category.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ApiResponses({
        @ApiResponse(responseCode = "401", description = "No autenticado (Falta token o es invalido)"),
        @ApiResponse(responseCode = "403", description = "No autorizado (No tienes permisos)"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
})
@RestController
@Slf4j
@RequestMapping("/v1/category")
@RequiredArgsConstructor
@Tag(name = "Category API", description = "Endpoints para la gestion de categorias")
@ApiResponses(value = {
    @ApiResponse(responseCode = "403", description = "No autorizado - Token JWT ausente, expirado o inválido", 
                 content = @Content(mediaType = "application/json")),
    @ApiResponse(responseCode = "500", description = "Error interno - Error inesperado en el servidor", 
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorApiResponse.class))),
})
public class CategoryController {

    private final CategoryService service;

    @Operation(summary = "Obtener todas las categorias", description = "Retorna una lista con todas las categorias registradas en la base de datos.")
    @ApiResponse(responseCode = "200", description = "Lista de categorias obtenida correctamente")
    @GetMapping
    public ResponseEntity<List<Category>> getCategoria() {
        log.info("GET api/category/buscarCategorias");
        return ResponseEntity.ok().body(service.buscarCategorias());
    }

    @Operation(summary = "Buscar categoria por ID", description = "Busca y retorna los detalles de una categoria especifica.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "categoria encontrada"),
            @ApiResponse(responseCode = "404", description = "categoria no encontrada")
    })
    @GetMapping("/porid/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategoriaPorId(@Parameter(description = "Identificador único de la categoria", example = "1") @PathVariable Long id) {
        log.info("GET api/category/categoriaPorId/{}", id);
        return ResponseEntity.ok().body(service.buscarCategoriaPorId(id));
    }

    @Operation(summary = "Eliminar categoria", description = "Elimina una categoria existente mediante su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Categoria eliminada con exito (Sin contenido)"),
            @ApiResponse(responseCode = "404", description = "Categoria no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoria(@Parameter(description = "Identificador único de la categoria", example = "1") @PathVariable Long id) {
        log.info("DELETE api/category/eliminarCategoria/{}", id);
        service.eliminarCategoria(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Modificar categoria", description = "Actualiza los datos de una categoria existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoria actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o nombre duplicado"),
            @ApiResponse(responseCode = "404", description = "Categoria no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> updateCategoria(
            @Parameter(description = "Identificador único de la categoria", example = "1") @PathVariable Long id,
            @Valid @RequestBody CategoryRequest category) {
        log.info("PUT api/category/{}/modificarCategoria", id);
        return ResponseEntity.ok(service.modificarCategoria(id, category));
    }

    @Operation(summary = "Crear nueva categoria", description = "Registra una nueva categoria en el sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Categoria creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o la categoria ya existe"),
            @ApiResponse(responseCode = "409", description = "Conflicto con base de datos", 
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorApiResponse.class)))
    })
    @PostMapping
    public ResponseEntity<CategoryResponseDTO> crearCategoria(@Valid @RequestBody CategoryRequest category){
        log.info("POST /api/category/creandoCategoria");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crearCategoria(category));
    }
}
