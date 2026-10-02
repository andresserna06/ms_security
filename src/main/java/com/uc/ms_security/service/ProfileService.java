package com.uc.ms_security.service;

import com.uc.ms_security.dto.profile.CreateProfileDTO;
import com.uc.ms_security.dto.profile.ProfileResponseDTO;
import com.uc.ms_security.dto.profile.UpdateProfileDTO;
import com.uc.ms_security.entity.Profile;
import com.uc.ms_security.entity.User;
import com.uc.ms_security.mapper.ProfileMapper;
import com.uc.ms_security.repository.ProfileRepository;
import com.uc.ms_security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final ProfileMapper profileMapper;

    public ProfileResponseDTO create(CreateProfileDTO dto) {
        // 1. Validar que el usuario exista
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuario no encontrado con ID: " + dto.getUserId()
                ));

        // 2. Validar que el usuario no tenga ya un perfil asignado (Relación 1 a 1)
        if (profileRepository.existsByUserId(dto.getUserId())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El usuario ya cuenta con un perfil asignado"
            );
        }

        // 3. Validar que el teléfono no esté duplicado
        if (profileRepository.existsByPhone(dto.getPhone())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe un perfil con este número de teléfono"
            );
        }

        Profile profile = profileMapper.toEntity(dto, user);
        Profile savedProfile = profileRepository.save(profile);
        return profileMapper.toResponseDTO(savedProfile);
    }

    public List<ProfileResponseDTO> findAll() {
        List<Profile> profiles = profileRepository.findAll();
        return profileMapper.toResponseDTOList(profiles);
    }

    private Profile findProfile(Long id) {
        return profileRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Perfil no encontrado"
                ));
    }

    public ProfileResponseDTO findById(Long id) {
        Profile profile = findProfile(id);
        return profileMapper.toResponseDTO(profile);
    }


    public ProfileResponseDTO update(Long id, UpdateProfileDTO dto) {
        Profile profile = findProfile(id);

        if (profileRepository.existsByPhoneAndIdNot(dto.getPhone(), id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El número de teléfono pertenece a otro perfil"
            );
        }

        profileMapper.updateEntity(dto, profile);
        Profile updatedProfile = profileRepository.save(profile);
        return profileMapper.toResponseDTO(updatedProfile);
    }

    public void delete(Long id) {
        Profile profile = findProfile(id);
        profileRepository.delete(profile);
    }
}
