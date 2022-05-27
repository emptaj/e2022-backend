package com.example.store.repository;

import com.example.store.entity.WarehouseEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseRepository extends JpaRepository<WarehouseEntity, Long> {
    
    Page<WarehouseEntity> findAllByActive(boolean active, Pageable p);

}
