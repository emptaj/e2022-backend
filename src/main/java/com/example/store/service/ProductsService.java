package com.example.store.service;

import java.util.List;
import java.util.stream.Collectors;

import com.example.store.converter.ProductsConverter;
import com.example.store.model.ProductModel;
import com.example.store.repository.ProductRepository;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductsService {

    private final ProductRepository repository;
    private final ProductsConverter converter;

    public List<ProductModel> getProducts() {
        return repository.findAll().stream()
                .map(converter::toModel)
                .collect(Collectors.toList());
    }    
}
