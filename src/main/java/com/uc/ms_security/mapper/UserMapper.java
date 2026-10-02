package com.uc.ms_security.mapper;

import com.uc.ms_security.dto.user.CreateUserDTO;
import com.uc.ms_security.dto.user.UpdateUserDTO;
import com.uc.ms_security.dto.user.UserResponseDTO;
import com.uc.ms_security.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

    public User toEntity(CreateUserDTO dto) { // Recibe DTO de fichitas a muñeco
        User user = new User();

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());

        return user;
    }

    public void updateEntity(UpdateUserDTO dto, User user) {
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());

        if (dto.getPassword() != null) {
            user.setPassword(dto.getPassword());
        }
    }

    public UserResponseDTO toResponseDTO(User user) { // De objeto a fichitas, en este caso el DTO se encargo de no mostrar la contraseña
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public List<UserResponseDTO> toResponseDTOList(List<User> users) {
        return users.stream()
                .map(this::toResponseDTO)
                .toList();
    }
}