package com.uc.ms_security.service;

import com.uc.ms_security.dto.user.CreateUserDTO;
import com.uc.ms_security.dto.user.UpdateUserDTO;
import com.uc.ms_security.dto.user.UserDetailResponseDTO;
import com.uc.ms_security.dto.user.UserResponseDTO;
import com.uc.ms_security.dto.user.UserRolesResponseDTO;
import com.uc.ms_security.dto.user.UserSessionsResponseDTO;
import com.uc.ms_security.entity.User;
import com.uc.ms_security.exception.ApplicationException;
import com.uc.ms_security.exception.ErrorCase;
import com.uc.ms_security.mapper.UserMapper;
import com.uc.ms_security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder; // Inyección de passwordEncoder, visto en el decorador Bean

    public UserResponseDTO create(CreateUserDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ApplicationException(
                    ErrorCase.ALREADY_EXISTS,
                    "El correo ya está registrado"
            );
        }

        String encodedPassword = passwordEncoder.encode(
                dto.getPassword()
        );

        User user = userMapper.toEntity(
                dto,
                encodedPassword
        );

        User savedUser = userRepository.save(user);

        return userMapper.toResponseDTO(savedUser);
    }

    public List<UserResponseDTO> findAll() {
        return userMapper.toResponseDTOList(
                userRepository.findAll()
        );
    }

    public UserResponseDTO findById(Long id) {
        return userMapper.toResponseDTO(
                findEntityById(id)
        );
    }

    public UserResponseDTO update(
            Long id,
            UpdateUserDTO dto) {

        User user = findEntityById(id);

        if (userRepository.existsByEmailAndIdNot(
                dto.getEmail(),
                id)) {
            throw new ApplicationException(
                    ErrorCase.ALREADY_EXISTS,
                    "El correo ya está registrado"
            );
        }

        userMapper.updateBasicData(dto, user);

        if (dto.getPassword() != null
                && !dto.getPassword().isBlank()) {

            String encodedPassword = passwordEncoder.encode(
                    dto.getPassword()
            );

            user.setPassword(encodedPassword);
        }

        User updatedUser = userRepository.save(user);

        return userMapper.toResponseDTO(updatedUser);
    }

    public void delete(Long id) {
        User user = findEntityById(id);
        userRepository.delete(user);
    }

    private User findEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(
                        () -> new ApplicationException(
                                ErrorCase.NOT_FOUND,
                                "Usuario no encontrado"
                        )
                );
    }

    public UserDetailResponseDTO findByIdWithProfile(Long id) {
        User user = userRepository.findWithProfileById(id)
                .orElseThrow(() -> new ApplicationException(
                        ErrorCase.NOT_FOUND,
                        "Usuario no encontrado con id: " + id
                ));
        return userMapper.toDetailResponseDTO(user);
    }

    // Buscar las sesiones de un usuario
    public UserSessionsResponseDTO findByIdAndSessions(Long id) {
        User user = userRepository
                .findWithSessionsById(id)
                .orElseThrow(
                        () -> new ApplicationException(
                                ErrorCase.NOT_FOUND,
                                "Usuario no encontrado con id: " + id
                        )
                );

        return userMapper.toSessionsResponseDTO(user);
    }

    public UserRolesResponseDTO findByIdAndRoles(Long id) {
        User user = userRepository
                .findWithRolesById(id)
                .orElseThrow(
                        () -> new ApplicationException(
                                ErrorCase.NOT_FOUND,
                                "Usuario no encontrado con id: " + id
                        )
                );

        return userMapper.toRolesResponseDTO(user);
    }
}
