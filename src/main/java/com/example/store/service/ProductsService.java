package com.example.store.service;

import java.util.List;
import java.util.stream.Collectors;

import com.example.store.converter.ProductsConverter;
import com.example.store.entity.ProductEntity;
import com.example.store.exception.NotFoundException;
import com.example.store.exception.ValidationException;
import com.example.store.model.ProductModel;
import com.example.store.repository.ProductRepository;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductsService {

    private final ProductRepository repository;
    private final ProductsConverter converter;


    public ProductEntity findProductById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(ProductEntity.class, id));
    }


    public List<ProductModel> getProducts() {
        return repository.findAll().stream()
                .map(converter::toModel)
                .collect(Collectors.toList());
    }

    public ProductModel getProduct(Long id) {
        ProductEntity entity = findProductById(id);
        return converter.toModel(entity);
    }


    public ProductModel createProduct(ProductModel product) {
        validateInput(product);
        ProductEntity entity = converter.create(product);
        entity = repository.save(entity);
        return converter.toModel(entity);
    }

    private void validateInput(ProductModel product) {
        if (!StringUtils.hasText(product.getName()))
            throw new ValidationException("Product name cannot be empty");
        if (!StringUtils.hasText(product.getDescription()))
            throw new ValidationException("Product description cannot be empty");
        if (product.getWarehouseId() == null)
            throw new ValidationException("Product warehouse cannot be empty");
        if (product.getPrice() == null)
            throw new ValidationException("Product price cannot be empty");
        if (product.getPrice() < 0.0f)
            throw new ValidationException("Product price cannot be negative");
    }


    public ProductModel updateProduct(Long productId, ProductModel product) {
        ProductEntity entity = findProductById(productId);
        validateInput(product);
        converter.update(entity, product);
        return converter.toModel(entity);
    }


    public void deleteProduct(Long productId) {
        ProductEntity entity = findProductById(productId);
        entity.setActive(false);
        repository.save(entity);
    }
}
