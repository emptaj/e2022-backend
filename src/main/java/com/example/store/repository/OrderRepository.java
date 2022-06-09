package com.example.store.repository;

import com.example.store.entity.OrderEntity;
import com.example.store.entity.enums.OrderState;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long>{
    
    Page<OrderEntity> findAllByUserIdAndStateNotIn(Long userId, List<OrderState> states, Pageable p);

    Page<OrderEntity> findAllByWarehouseIdAndStateIn(Long warehouseId, List<OrderState> states, Pageable p);
}
