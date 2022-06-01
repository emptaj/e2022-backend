package com.example.store.repository;

import org.springframework.stereotype.Repository;
import com.example.store.entity.DeliveryTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface DeliveryTypeRepository extends JpaRepository<DeliveryTypeEntity, Long> {
    
}
