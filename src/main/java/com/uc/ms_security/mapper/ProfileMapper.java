package com.uc.ms_security.mapper;

import com.uc.ms_security.dto.profile.CreateProfileDTO;
import com.uc.ms_security.dto.profile.ProfileResponseDTO;
import com.uc.ms_security.dto.profile.UpdateProfileDTO;
import com.uc.ms_security.entity.Profile;
import com.uc.ms_security.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProfileMapper {

    public Profile toEntity(CreateProfileDTO dto, User user) { // Recibe DTO y entidad User para asignar la relación
        Profile profile = new Profile();

        profile.setPhone(dto.getPhone());
        profile.setBirthDate(dto.getBirthDate());
        profile.setUser(user);

        return profile;
    }

    public void updateEntity(UpdateProfileDTO dto, Profile profile) { // Modificación por referencia
        profile.setPhone(dto.getPhone());
        profile.setBirthDate(dto.getBirthDate());
    }

    public ProfileResponseDTO toResponseDTO(Profile profile) { // Mapeo a DTO de respuesta
        return new ProfileResponseDTO(
                profile.getId(),
                profile.getPhone(),
                profile.getBirthDate(),
                profile.getUser() != null ? profile.getUser().getId() : null
        );
    }

    public List<ProfileResponseDTO> toResponseDTOList(List<Profile> profiles) {
        return profiles.stream()
                .map(this::toResponseDTO)
                .toList();
    }
}
