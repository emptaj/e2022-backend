package com.example.store.controller;

import javax.transaction.Transactional;
import javax.validation.Valid;

import com.example.store.dto.ListDTO;
import com.example.store.dto.product.ProductDTO;
import com.example.store.dto.product.ProductExDTO;
import com.example.store.dto.product.UpdateProductDTO;
import com.example.store.service.ProductService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@Valid
@RestController
@RequestMapping(path = "/api")
@Transactional
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;


    @GetMapping("/products")
    ListDTO<ProductDTO> getProducts(@RequestParam(required = false, defaultValue = "0") int page,
                                    @RequestParam(required = false, defaultValue = "20") int size) {
        return service.getProducts(page, size);
    }

    @GetMapping("/warehouses/{warehouseId}/products")
    ListDTO<ProductExDTO> getProductsEx(@PathVariable Long warehouseId,
                                        @RequestParam(required = false, defaultValue = "true") boolean onlyActive,
                                        @RequestParam(required = false, defaultValue = "0") int page,
                                        @RequestParam(required = false, defaultValue = "20") int size) {
        return service.getProductsEx(warehouseId, onlyActive, page, size);
    }

    @GetMapping("/products/{productId}")
    ProductDTO getProduct(@PathVariable Long productId) {
        return service.getProduct(productId);
    }

    @GetMapping("/products/{productId}/extended")
    ProductExDTO getProductEx(@PathVariable Long productId) {
        return service.getProductEx(productId);
    }

    @DeleteMapping("/products/{productId}")
    void deleteProduct(@PathVariable Long productId) {
        service.deleteProduct(productId);
    }

    @PostMapping("/warehouses/{warehouseId}/products")
    @ResponseStatus(value = HttpStatus.CREATED)
    ProductDTO createProduct(@PathVariable Long warehouseId,
                             @Valid @RequestBody UpdateProductDTO product) {
        return service.createProduct(warehouseId, product);
    }

    @PutMapping("/products/{productId}")
    ProductDTO updateProduct(@PathVariable Long productId,
                             @Valid @RequestBody UpdateProductDTO product) {
        return service.updateProduct(productId, product);
    }

    @PutMapping("/products/{productId}/stock")
    ProductExDTO restockProduct(@PathVariable Long productId,
                                @RequestParam Integer stock) {
        return service.restockProduct(productId, stock);
    }
}
