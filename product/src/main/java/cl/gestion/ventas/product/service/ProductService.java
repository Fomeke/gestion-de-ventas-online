package cl.gestion.ventas.product.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.gestion.ventas.product.client.CategoryClient;
import cl.gestion.ventas.product.dto.ProductRequestDTO;
import cl.gestion.ventas.product.dto.ProductResponseDTO;
import cl.gestion.ventas.product.mapper.ProductMapper;
import cl.gestion.ventas.product.model.Product;
import cl.gestion.ventas.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class ProductService {

    @Autowired
    private ProductMapper mapper;

    @Autowired
    private ProductRepository repo;

    @Autowired
    private CategoryClient client;

    public List<Product> buscarProductos() {
        log.info("Buscando productos..");
        return repo.findAll();
    }

    public ProductResponseDTO productoPorId(Long id) {
        log.info("Buscando producto por su id..");
        Product product = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Producto no encontrado."));
        return mapper.toResponse(product);
    }

    public ProductResponseDTO crearProducto(ProductRequestDTO request,String token) {
        log.info("Creando producto con nombre: {}", request.getName());
        if (repo.existsByName(request.getName())) {
            throw new IllegalArgumentException("El nombre de ese producto ya existe.");
        }
        client.obtenerProductoPorCategoriaId(request.getCategoryId(), token);
        Product product = repo.save(mapper.fromRequest(request));
        return mapper.toResponse(product);
    }

    public ProductResponseDTO modificarProducto(Long id, ProductRequestDTO request,String token) {
        log.info("Modificando producto con id: {}", id);

        Product product = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Producto no encontrado."));

        if(request.getName() != null){
            product.setName(request.getName());
        }
        if(request.getPrice() != null){
            product.setPrice(request.getPrice());
        }
        if(request.getDescription() != null){
            product.setDescription(request.getDescription());
        }
        if(request.getCategoryId() != null){
            client.obtenerProductoPorCategoriaId(request.getCategoryId(), token);
            product.setCategoryId(request.getCategoryId());
        }

        

        product = repo.save(product);
        return mapper.toResponse(product);
    }

    public void eliminarProducto(Long id){
        log.info("Eliminando producto con id: {}", id);
        if(!repo.existsById(id)){
            throw new NoSuchElementException("Producto no encontrado.");
        }
        repo.deleteById(id);
    }

}
