package com.example.store.repository;

import com.example.store.entity.RegistrationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegistrationTokenRepository extends JpaRepository<RegistrationTokenEntity, Long> {

    Optional<RegistrationTokenEntity> findByToken(String activateToken);
}
