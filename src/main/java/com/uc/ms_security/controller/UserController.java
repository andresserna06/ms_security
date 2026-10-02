package com.uc.ms_security.controller;

import com.uc.ms_security.dto.user.CreateUserDTO;
import com.uc.ms_security.dto.user.UpdateUserDTO;
import com.uc.ms_security.dto.user.UserResponseDTO;
import com.uc.ms_security.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService; // Inyección de dependencia

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO create(@Valid @RequestBody CreateUserDTO dto) {

        return userService.create(dto);
    }

    @GetMapping
    public List<UserResponseDTO> findAll() {

        return userService.findAll(); // Revisar la paginación en caso de tener muchos usuarios, indicarle al servicio que lo devuelva paginado
    }

    @GetMapping("/{id}")
    public UserResponseDTO findById(@PathVariable Long id) {

        return userService.findById(id);
    }

    @PutMapping("/{id}")
    public UserResponseDTO update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserDTO dto) {
        return userService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { // @PathVariable - Obtener el edintificador de la ruta en una variable

        userService.delete(id);
    }
}