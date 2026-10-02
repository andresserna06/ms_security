package com.uc.ms_security.mapper;

import com.uc.ms_security.dto.session.CreateSessionDTO;
import com.uc.ms_security.dto.session.SessionResponseDTO;
import com.uc.ms_security.dto.session.UpdateSessionDTO;
import com.uc.ms_security.entity.Session;
import com.uc.ms_security.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SessionMapper {

    public Session toEntity(CreateSessionDTO dto, User user) { // Recibe DTO y entidad User para vincular la sesión
        Session session = new Session();

        session.setToken(dto.getToken());
        session.setExpiration(dto.getExpiration());
        session.setCode2FA(dto.getCode2FA());
        session.setUser(user);

        return session;
    }

    public void updateEntity(UpdateSessionDTO dto, Session session) { // Modificación por referencia
        session.setToken(dto.getToken());
        session.setExpiration(dto.getExpiration());
        session.setCode2FA(dto.getCode2FA());
    }

    public SessionResponseDTO toResponseDTO(Session session) { // Mapeo a DTO de respuesta
        return new SessionResponseDTO(
                session.getId(),
                session.getToken(),
                session.getExpiration(),
                session.getCode2FA(),
                session.getUser() != null ? session.getUser().getId() : null
        );
    }

    public List<SessionResponseDTO> toResponseDTOList(List<Session> sessions) {
        return sessions.stream()
                .map(this::toResponseDTO)
                .toList();
    }
}
