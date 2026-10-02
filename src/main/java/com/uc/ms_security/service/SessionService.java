package com.uc.ms_security.service;

import com.uc.ms_security.dto.session.CreateSessionDTO;
import com.uc.ms_security.dto.session.SessionResponseDTO;
import com.uc.ms_security.dto.session.UpdateSessionDTO;
import com.uc.ms_security.entity.Session;
import com.uc.ms_security.entity.User;
import com.uc.ms_security.mapper.SessionMapper;
import com.uc.ms_security.repository.SessionRepository;
import com.uc.ms_security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final SessionMapper sessionMapper;

    public SessionResponseDTO create(CreateSessionDTO dto) {
        // 1. Validar que el usuario exista
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuario no encontrado con ID: " + dto.getUserId()
                ));

        // 2. Validar que no exista otra sesión con el mismo token
        if (sessionRepository.existsByToken(dto.getToken())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe una sesión con este token"
            );
        }

        // 3. Crear entidad vinculando al usuario (permite múltiples sesiones por usuario: relación 1 a N)
        Session session = sessionMapper.toEntity(dto, user);
        Session savedSession = sessionRepository.save(session);
        return sessionMapper.toResponseDTO(savedSession);
    }

    public List<SessionResponseDTO> findAll() {
        List<Session> sessions = sessionRepository.findAll();
        return sessionMapper.toResponseDTOList(sessions);
    }

    private Session findSession(Long id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Sesión no encontrada"
                ));
    }

    public SessionResponseDTO findById(Long id) {
        Session session = findSession(id);
        return sessionMapper.toResponseDTO(session);
    }


    public SessionResponseDTO update(Long id, UpdateSessionDTO dto) {
        Session session = findSession(id);

        if (sessionRepository.existsByTokenAndIdNot(dto.getToken(), id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El token pertenece a otra sesión"
            );
        }

        sessionMapper.updateEntity(dto, session);
        Session updatedSession = sessionRepository.save(session);
        return sessionMapper.toResponseDTO(updatedSession);
    }

    public void delete(Long id) {
        Session session = findSession(id);
        sessionRepository.delete(session);
    }
}
