package com.example.store.service;

import com.example.store.dto.order.OrderDTO;
import com.example.store.dto.order.OrderDetailsDTO;
import com.example.store.entity.OrderEntity;
import com.example.store.exception.NotFoundException;
import com.example.store.mapper.OrderMapper;
import com.example.store.repository.OrderRepository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
    
    private final OrderRepository repository;
    private final OrderMapper mapper = OrderMapper.INSTANCE;


    public OrderEntity findOrderById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(OrderEntity.class, id));
    }


    public OrderDTO getOrder(Long orderId) {
        OrderEntity entity = findOrderById(orderId);
        List<OrderDetailsDTO> detailsDTO = entity.getOrderDetails().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());

        return mapper.toDTO(entity, detailsDTO);
    }


    
}
