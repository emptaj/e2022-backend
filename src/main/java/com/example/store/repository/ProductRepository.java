package com.example.store.repository;

import com.example.store.entity.ProductEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    Page<ProductEntity> findAllByActive(boolean active, Pageable p);

    Page<ProductEntity> findAllByWarehouseId(Long warehouseId, Pageable p);

    Page<ProductEntity> findAllByWarehouseIdAndActive(Long warehouseId, Boolean active, Pageable p);
}
