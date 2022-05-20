package com.example.store.controller;

import java.util.List;

import javax.transaction.Transactional;

import com.example.store.dto.product.ProductDTO;
import com.example.store.dto.product.UpdateProductDTO;
import com.example.store.service.ProductsService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/api")
@CrossOrigin
@Transactional
@RequiredArgsConstructor
public class ProductsController {

    private final ProductsService service;


    @GetMapping("/products")
    List<ProductDTO> getProducts(@RequestParam(required = false, defaultValue = "0") int page,
                                 @RequestParam(required = false, defaultValue = "20") int size) {
        return service.getProducts(page, size);
    }

    @GetMapping("/products/{productId}")
    ProductDTO getProduct(@PathVariable Long productId) {
        return service.getProduct(productId);
    }

    @DeleteMapping("/products/{productId}")
    void deleteProduct(@PathVariable Long productId) {
        service.deleteProduct(productId);
    }

    @PostMapping("/warehouses/{warehouseId}/products")
    @ResponseStatus(value = HttpStatus.CREATED)
    ProductDTO createProduct(@PathVariable Long warehouseId,
                             @RequestBody UpdateProductDTO product) {
        return service.createProduct(warehouseId, product);
    }

    @PutMapping("/products/{productId}")
    ProductDTO updateProduct(@PathVariable Long productId,
                             @RequestBody UpdateProductDTO product) {
        return service.updateProduct(productId, product);
    }

}
