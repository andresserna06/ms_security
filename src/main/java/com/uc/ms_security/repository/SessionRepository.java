package com.uc.ms_security.repository;

import com.uc.ms_security.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {

    boolean existsByToken(String token);

    boolean existsByTokenAndIdNot(String token, Long id);

    Optional<Session> findByToken(String token);

    List<Session> findByUserId(Long userId);
}
