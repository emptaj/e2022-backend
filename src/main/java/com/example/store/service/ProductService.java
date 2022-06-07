package com.example.store.service;

import com.example.store.dto.ListDTO;
import com.example.store.dto.product.ProductDTO;
import com.example.store.dto.product.ProductExDTO;
import com.example.store.dto.product.UpdateProductDTO;
import com.example.store.entity.ProductEntity;
import com.example.store.entity.WarehouseEntity;
import com.example.store.mapper.ProductMapper;
import com.example.store.repository.ProductRepository;
import com.example.store.repository.WarehouseRepository;
import com.example.store.repository.finder.RecordFinder;
import com.example.store.validator.Validator;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper = ProductMapper.INSTANCE;
    private final RecordFinder<ProductEntity, ProductRepository> finder;
    private final RecordFinder<WarehouseEntity, WarehouseRepository> warehouseFinder;


    public ListDTO<ProductDTO> getProducts(int page, int size) {
        Page<ProductEntity> pageResponse = repository.findAllByActive(true, PageRequest.of(page, size));

        List<ProductDTO> products = pageResponse.getContent().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());

        return new ListDTO<ProductDTO>(pageResponse.getTotalPages(), products);
    }

    public ProductDTO getProduct(Long id) {
        ProductEntity entity = finder.byId(id);
        return mapper.toDTO(entity);
    }


    public ProductDTO createProduct(Long warehouseId, UpdateProductDTO product) {
        WarehouseEntity warehouse = warehouseFinder.byId(warehouseId);
        Validator.positiveValue(warehouse.getActive(), "Cannot create product for deleted warehouse");

        ProductEntity entity = mapper.create(product, warehouse);
        entity = repository.save(entity);
        return mapper.toDTO(entity);
    }

    public ProductDTO updateProduct(Long productId, UpdateProductDTO product) {
        Validator.validate(product);
        ProductEntity entity = finder.byId(productId);
        Validator.positiveValue(entity.getActive(), "Cannot update removed product");
        entity = mapper.update(product, entity);
        return mapper.toDTO(entity);
    }


    public void deleteProduct(Long productId) {
        ProductEntity entity = finder.byId(productId);
        Validator.positiveValue(entity.getActive(), "Product already deleted");
        entity.setActive(false);
        repository.save(entity);
    }


    public ListDTO<ProductExDTO> getProductsEx(Long warehouseId, boolean onlyActive, int page, int size) {
        warehouseFinder.byId(warehouseId);
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<ProductEntity> pageResponse;
        if (onlyActive)
            pageResponse = repository.findAllByWarehouseIdAndActive(warehouseId, true, pageRequest);
        else
            pageResponse = repository.findAllByWarehouseId(warehouseId, pageRequest);

        List<ProductExDTO> products = pageResponse.getContent().stream()
                .map(mapper::toExDTO)
                .collect(Collectors.toList());

        return new ListDTO<ProductExDTO>(pageResponse.getTotalPages(), products);
    }


    public ProductExDTO getProductEx(Long productId) {
        ProductEntity entity = finder.byId(productId);
        return mapper.toExDTO(entity);
    }


    public ProductExDTO restockProduct(Long productId, Integer stock) {
        ProductEntity entity = finder.byId(productId);
        Validator.notNegative(stock, "Nr products in stock cannot be negative");
        entity = mapper.restock(entity, stock);
        entity = repository.save(entity);
        return mapper.toExDTO(entity);
    }

    
    public void sendProduct(ProductEntity entity, Integer quantity) {
        Validator.positiveValue(quantity, "Nr products to send must be positive");
        Validator.notNegative(entity.getUnitsInStock() - quantity, "There are not enough items in stock to send");
        entity = mapper.send(entity, quantity);
        repository.save(entity);
    }


    public void orderProduct(ProductEntity entity, Integer quantity) {
        Validator.positiveValue(entity.getActive(), "Cannot order removed product");
        Validator.positiveValue(quantity, "Nr products to order must be positive");
        entity = mapper.order(entity, quantity);
        repository.save(entity);
    }

    
    public void removeProductOrder(ProductEntity entity, Integer quantity) {
        entity = mapper.order(entity, -quantity);
        repository.save(entity);
    }
}
