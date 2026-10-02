package com.uc.ms_security.repository;

import com.uc.ms_security.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    boolean existsByUserId(Long userId);

    boolean existsByPhone(String phone);

    boolean existsByPhoneAndIdNot(String phone, Long id);

    Optional<Profile> findByUserId(Long userId);
}
