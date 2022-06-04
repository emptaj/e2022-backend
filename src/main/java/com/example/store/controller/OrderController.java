package com.example.store.controller;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.store.dto.ListDTO;
import com.example.store.dto.order.CreateOrderDTO;
import com.example.store.dto.order.OrderDTO;
import com.example.store.entity.enums.OrderState;
import com.example.store.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@Transactional
@RequiredArgsConstructor
public class OrderController {
    
    private final OrderService service;


    @GetMapping("/orders/{orderId}")
    public OrderDTO getOrder(@PathVariable Long orderId) {
        return service.getOrder(orderId);
    }

    @PostMapping("/orders")
    public List<OrderDTO> createOrder(@RequestBody CreateOrderDTO dto) {
        return service.createOrder(dto);
    }

    @GetMapping("users/{userId}/orders/")
    public ListDTO<OrderDTO> getUserOrders(@PathVariable Long userId,
                                           @RequestParam(required = false, defaultValue = "0")    int page,
                                           @RequestParam(required = false, defaultValue = "20")   int size) {
        return service.getUserOrders(userId, page, size);
    }
    
    @PutMapping("/orders/{orderId}")
    public OrderDTO changeOrderState(@PathVariable Long orderId, @RequestParam OrderState nextState) {
        return service.changeOrderState(orderId, nextState);
    }
}
