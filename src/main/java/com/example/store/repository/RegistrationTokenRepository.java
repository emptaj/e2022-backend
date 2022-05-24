package com.example.store.repository;

import com.example.store.entity.RegistrationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationTokenRepository extends JpaRepository<RegistrationTokenEntity, Long> {
}
