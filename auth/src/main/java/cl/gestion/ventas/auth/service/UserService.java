package cl.gestion.ventas.auth.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.gestion.ventas.auth.dto.UserRequest;
import cl.gestion.ventas.auth.dto.UserResponse;
import cl.gestion.ventas.auth.mapper.UserMapper;
import cl.gestion.ventas.auth.model.User;
import cl.gestion.ventas.auth.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * UserService es la clase que maneja la capa de negocios de los usuarios,
 * tiene métodos para retornar todos los usuarios, buscar por Id, nombre,
 * y operaciones CRUD provienientes del repositorio pero aplicando reglas
 * para que, por ejemplo, no existan duplicados al momento de crear un usuario.
 * Aplica el Mapper para retornar objetos DTO hacia el controlador.
 */

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    public List<UserResponse> obtenerUsuarios(){
        log.info("Obteniendo todos los usuarios");
        List<User> users = userRepository.findAll();
        return users.stream().map(userMapper::toResponse).toList();
    }

    public UserResponse obtenerUsuarioPorId(Long id){
        log.info("Obteniendo usuario con id: {}",id);
        User user = userRepository.findById(id).get();
        return userMapper.toResponse(user);
    }

    public UserResponse obtenerUsurarioPorNombre(String username){
        log.info("Obtentiendo usuario con nombre de usuario: {}",username);
        User user = userRepository.findByUsername(username).get();
        return userMapper.toResponse(user);
    }

    public UserResponse crear(UserRequest userRequest){
        log.info("Creando nuevo usuario");
        if(userRepository.existsByUsername(userRequest.getUsername()) || userRepository.existsByFullName(userRequest.getFullName())){
            throw new IllegalArgumentException("Usuario ya existe");
        }
        User user = userRepository.save(userMapper.fromRequest(userRequest));
        return userMapper.toResponse(user);
    }

    public void eliminar(Long id){
        log.info("Eliminando usuario con ID: {}",id);
        if(!userRepository.existsById(id)){
            throw new NoSuchElementException("Usuario no encontrado con id: "+id);
        }
        userRepository.deleteById(id);
    }
    
}
