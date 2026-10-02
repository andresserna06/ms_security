package com.uc.ms_security.service;

import com.uc.ms_security.dto.role.CreateRoleDTO;
import com.uc.ms_security.dto.role.RoleResponseDTO;
import com.uc.ms_security.dto.role.UpdateRoleDTO;
import com.uc.ms_security.entity.Role;
import com.uc.ms_security.mapper.RoleMapper;
import com.uc.ms_security.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleResponseDTO create(CreateRoleDTO dto) {
        if (roleRepository.existsByName(dto.getName())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe un rol con este nombre"
            );
        }

        Role role = roleMapper.toEntity(dto);
        Role savedRole = roleRepository.save(role);
        return roleMapper.toResponseDTO(savedRole);
    }

    public List<RoleResponseDTO> findAll() {
        List<Role> roles = roleRepository.findAll();
        return roleMapper.toResponseDTOList(roles);
    }

    private Role findRole(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Rol no encontrado"
                ));
    }

    public RoleResponseDTO findById(Long id) {
        Role role = findRole(id);
        return roleMapper.toResponseDTO(role);
    }


    public RoleResponseDTO update(Long id, UpdateRoleDTO dto) {
        Role role = findRole(id);

        if (roleRepository.existsByNameAndIdNot(dto.getName(), id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El nombre de rol ya pertenece a otro registro"
            );
        }

        roleMapper.updateEntity(dto, role);
        Role updatedRole = roleRepository.save(role);
        return roleMapper.toResponseDTO(updatedRole);
    }

    public void delete(Long id) {
        Role role = findRole(id);
        roleRepository.delete(role);
    }
}
