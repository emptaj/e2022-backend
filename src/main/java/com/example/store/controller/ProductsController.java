package com.example.store.controller;

import java.util.List;

import javax.transaction.Transactional;

import com.example.store.model.ProductModel;
import com.example.store.service.ProductsService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController("/products")
@Transactional
@RequiredArgsConstructor
public class ProductsController {

    private final ProductsService service;

    
    @GetMapping("")
    List<ProductModel> getProducts() {
        return service.getProducts();
    }
}
