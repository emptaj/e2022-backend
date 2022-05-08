package com.example.store.repository;

import com.example.store.entity.WarehouseEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseRepository extends JpaRepository<WarehouseEntity, Long> {
    
}
