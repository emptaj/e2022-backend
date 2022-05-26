package com.example.store.service;

import com.example.store.dto.product.ProductDTO;
import com.example.store.dto.product.ProductListDTO;
import com.example.store.dto.product.UpdateProductDTO;
import com.example.store.entity.ProductEntity;
import com.example.store.entity.WarehouseEntity;
import com.example.store.exception.NotFoundException;
import com.example.store.exception.ValidationException;
import com.example.store.mapper.ProductMapper;
import com.example.store.repository.ProductRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductsService {

    private final ProductRepository repository;
    private final ProductMapper mapper = ProductMapper.INSTANCE;

    private final WarehouseService warehouseService;


    public ProductEntity findProductById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(ProductEntity.class, id));
    }


    public ProductListDTO getProducts(int page, int size) {
        Page<ProductEntity> pageResponse = repository.findAllByActive(true, PageRequest.of(page, size));

        List<ProductDTO> collect = pageResponse.getContent().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());

        ProductListDTO productList = ProductListDTO.builder()
                .products(collect)
                .pageCount(Long.valueOf(pageResponse.getTotalPages()))
                .build();

        return productList;
    }

    public ProductDTO getProduct(Long id) {
        ProductEntity entity = findProductById(id);
        return mapper.toDTO(entity);
    }


    public ProductDTO createProduct(Long warehouseId, UpdateProductDTO product) {
        validateInput(product);
        WarehouseEntity warehouse = warehouseService.findWarehouseById(warehouseId);

        ProductEntity entity = mapper.createEntity(product, warehouse);
        entity = repository.save(entity);
        return mapper.toDTO(entity);
    }

    private void validateInput(UpdateProductDTO product) {
        if (!StringUtils.hasText(product.getName()))
            throw new ValidationException("Product name cannot be empty");
        if (!StringUtils.hasText(product.getDescription()))
            throw new ValidationException("Product description cannot be empty");
        if (product.getPrice() == null)
            throw new ValidationException("Product price cannot be empty");
        if (product.getPrice() < 0.0f)
            throw new ValidationException("Product price cannot be negative");
    }


    public ProductDTO updateProduct(Long productId, UpdateProductDTO product) {
        ProductEntity entity = findProductById(productId);
        validateInput(product);
        entity = mapper.updateEntity(product, entity);
        return mapper.toDTO(entity);
    }


    public void deleteProduct(Long productId) {
        ProductEntity entity = findProductById(productId);
        entity.setActive(false);
        repository.save(entity);
    }
}
