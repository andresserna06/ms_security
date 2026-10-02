package com.uc.ms_security.service;

import com.uc.ms_security.dto.user.CreateUserDTO;
import com.uc.ms_security.dto.user.UpdateUserDTO;
import com.uc.ms_security.dto.user.UserResponseDTO;
import com.uc.ms_security.entity.User;
import com.uc.ms_security.mapper.UserMapper;
import com.uc.ms_security.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserResponseDTO create(CreateUserDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, //Hay un error de single responsability en la capa del servició por el manejo de errores - Ya que obliga a unicaemnte usar API-REST y si quiere cambiar a MCP u otro medio se tendran que cambiar muchas lineas
                    "Ya existe un usuario con este email" // El que conoce de HTTP es el controlador
            );
        }
        User user = userMapper.toEntity(dto); // Este objeto es el que se pidio que se guarde - Aun no tiene un ID
        User savedUser = userRepository.save(user); // Este objeto es el que se acabo de guardar - Ya se le asigno un ID
        return userMapper.toResponseDTO(savedUser);
    }
    public List<UserResponseDTO> findAll() { // UserResponseDTO - Devuelve los usuarios sin la contraseña
        List<User> users =userRepository.findAll();
        return userMapper.toResponseDTOList(users);
    }
    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuario no encontrado"
                ));
    }

    public UserResponseDTO findById(Long id) {
        User user = findUser(id);
        return userMapper.toResponseDTO(user);
    }

    public UserResponseDTO update(Long id, UpdateUserDTO dto) {
        User user = findUser(id);
        if (userRepository.existsByEmailAndIdNot(dto.getEmail(), id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El email pertenece a otro usuario"
            );
        }
        userMapper.updateEntity(dto, user); // No hay que retornar porque el cambio fue por "referencia" (Se le envia la posición en memoria RAM donde esta alojado el objeto)
        User updatedUser = userRepository.save(user);
        return userMapper.toResponseDTO(updatedUser);
    }
    public void delete(Long id) {
        User user = findUser(id);
        userRepository.delete(user);
    }
}