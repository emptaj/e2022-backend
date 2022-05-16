package com.example.store.controller;

import java.util.List;

import javax.transaction.Transactional;

import com.example.store.model.ProductModel;
import com.example.store.service.ProductsService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/products")
@Transactional
@RequiredArgsConstructor
public class ProductsController {

    private final ProductsService service;


    @GetMapping("")
    List<ProductModel> getProducts() {
        return service.getProducts();
    }

    @GetMapping("/{productId}")
    ProductModel getProduct(@PathVariable Long productId) {
        return service.getProduct(productId);
    }

    @DeleteMapping("/{productId}")
    void deleteProduct(@PathVariable Long productId) {
        service.deleteProduct(productId);
    }

    @PostMapping("")
    @ResponseStatus(value = HttpStatus.CREATED)
    ProductModel createProduct(@RequestBody ProductModel product) {
        return service.createProduct(product);
    }

    @PutMapping("/{productId}")
    ProductModel updateProduct(@PathVariable Long productId,
                               @RequestBody ProductModel product) {
        return service.updateProduct(productId, product);
    }

}
