package cl.gestion.ventas.cart.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import cl.gestion.ventas.cart.client.ProductClient;
import cl.gestion.ventas.cart.dto.CartItemDTO;
import cl.gestion.ventas.cart.dto.CartRequest;
import cl.gestion.ventas.cart.dto.CartResponse;
import cl.gestion.ventas.cart.dto.ProductResponse;
import cl.gestion.ventas.cart.mapper.CartMapper;
import cl.gestion.ventas.cart.model.Cart;
import cl.gestion.ventas.cart.model.CartItem;
import cl.gestion.ventas.cart.repository.CartRepository;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {
    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartMapper cartMapper;

    @Mock
    private ProductClient productClient;

    @InjectMocks
    private CartService cartService;

    @Test
    public void testObtenerCarritos(){
        Cart cart = new Cart();
        CartResponse response = new CartResponse();
        when(cartRepository.findAll()).thenReturn(List.of(cart));
        when(cartMapper.toResponse(cart)).thenReturn(response);

        List<CartResponse> resultado = cartService.obtenerCarritos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(cartRepository,times(1)).findAll();
    }

    @Test
    public void testObtenerCarritoPorId(){
        Long userId = 10L;
        CartItem item = CartItem.builder().productId(11L).quantity(3).build();
        Cart cart = Cart.builder().userId(userId).items(List.of(item)).build();
        ProductResponse product = ProductResponse.builder().price(new BigDecimal("1500")).build();
        CartResponse response = new CartResponse();

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(productClient.getProductById(item.getProductId())).thenReturn(product);
        when(cartMapper.toResponse(cart)).thenReturn(response);
        
        CartResponse resultado = cartService.obtenerCarritoPorId(userId);

        assertNotNull(resultado);
        assertEquals(new BigDecimal("4500"), resultado.getTotal());
        assertEquals(1, resultado.getItems().size());
        verify(productClient,times(1)).getProductById(11L);
    }

    @Test
    public void testObtenerCarritoPorId_NoHayCarrito(){
        Long userId = 1L;
        
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());

        NoSuchElementException ex = assertThrows(NoSuchElementException.class, (Executable) () -> 
            cartService.obtenerCarritoPorId(userId));

        assertEquals("Carrito no encontrado", ex.getMessage());
        
        verify(cartRepository, times(1)).findByUserId(userId);
    }

    @Test
    public void testAgregarProductosACarrito_UsuarioNoAutorizado(){
        Long idUsuarioConToken = 99L;
        Long idDuenoCarro = 1L; // Son diferentes

        CartRequest request = CartRequest.builder().userId(idDuenoCarro).build();

        AccessDeniedException ex = assertThrows(AccessDeniedException.class, (Executable) () -> cartService.agregarProductosACarrito(idUsuarioConToken, request));

        assertEquals("No tiene permiso para modificar este carrito", ex.getMessage());
        verify(cartRepository,never()).save(any(Cart.class));
    }


    @Test
    public void testEliminarProductoDeCarrito(){
        Long userId = 1L;
        Long tokenUserId = 1L;
        Long productId = 67L;
        CartItem item = CartItem.builder().productId(productId).quantity(1).build();
        ArrayList<CartItem> list = new ArrayList<>();
        list.add(item);
        Cart cart = Cart.builder().userId(userId).items(list).build();

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(cart)).thenReturn(cart);

        assertDoesNotThrow((Executable) () -> cartService.eliminarProductoDeCarrito(userId, productId, tokenUserId));

        assertTrue(cart.getItems().isEmpty());
        verify(cartRepository,times(1)).save(cart);
    }

    @Test
    public void testEliminarCarrito(){
        Long id = 1L;
        Long tokenUserId = 1L;

        when(cartRepository.existsByUserId(id)).thenReturn(true);
        doNothing().when(cartRepository).deleteByUserId(id);

        cartService.eliminarCarrito(id, tokenUserId);
        verify(cartRepository,times(1)).deleteByUserId(id);
    }

    @Test
    public void testEliminarCarrito_NoExiste(){
        Long id = 5L;
        Long tokenUserId = 5L;

        when(cartRepository.existsByUserId(id)).thenReturn(false);

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            cartService.eliminarCarrito(id, tokenUserId);
        });

        assertEquals("Carrito no encontrado", exception.getMessage());

        verify(cartRepository, times(1)).existsByUserId(id);
        verify(cartRepository, never()).deleteByUserId(id);
    }

    @Test
    public void testActualizarCarrito(){
        Long userId = 1L;
        Long tokenUserId = 1L;

        CartItem oldItem = CartItem.builder().productId(67L).quantity(1).build();
        ArrayList<CartItem> list = new ArrayList<>();
        list.add(oldItem);
        Cart cart = Cart.builder().userId(userId).items(list).build();

        CartItemDTO newItem = CartItemDTO.builder().productId(69L).quantity(2).build();
        CartRequest request = new CartRequest(userId, List.of(newItem));

        ProductResponse product = ProductResponse.builder().price(new BigDecimal("10")).build();
        CartResponse response = new CartResponse();

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(productClient.getProductById(newItem.getProductId())).thenReturn(product);
        when(cartRepository.save(cart)).thenReturn(cart);
        when(cartMapper.toResponse(cart)).thenReturn(response);

        CartResponse resultado = cartService.actualizarCarrito(userId, request, tokenUserId);

        assertNotNull(resultado);
        assertEquals(1, cart.getItems().size());
        assertEquals(69L, cart.getItems().get(0).getProductId());
        verify(cartRepository,times(1)).save(cart);

    }

}
