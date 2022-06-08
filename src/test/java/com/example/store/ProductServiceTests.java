package com.example.store;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Collectors;

import com.example.store.Builder.ExampleDTOBuilder;
import com.example.store.EqualChecker.EqualDTOChecker;
import com.example.store.dto.product.ProductDTO;
import com.example.store.dto.product.UpdateProductDTO;
import com.example.store.dto.warehouse.CreateWarehouseDTO;
import com.example.store.dto.warehouse.WarehouseDTO;
import com.example.store.exception.NotFoundException;
import com.example.store.exception.ValidationException;
import com.example.store.service.ProductService;
import com.example.store.service.WarehouseService;
import com.example.store.validator.Validator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import javax.transaction.Transactional;

@SpringBootTest
public class ProductServiceTests {


    @Autowired
    private ProductService prodService;
    @Autowired
    private WarehouseService wareService;

    @Test
    @Transactional
    void getNonExistingProductByIdTest() throws NotFoundException{
        Long id = -1L;
        assertThrows(NotFoundException.class, () -> {
            prodService.getProduct(id);
        });
    }

    @Test
    @Transactional
    @WithMockUser(username="admin")
    void CreateProductTest(){
        UpdateProductDTO test = ExampleDTOBuilder.buildExampleProductDTO();
        CreateWarehouseDTO warehouseTest = ExampleDTOBuilder.buildExampleWarehouseDTO();
        WarehouseDTO warehouseDTOTest = wareService.createWarehouse(warehouseTest);
        ProductDTO generatedProduct = prodService.createProduct(warehouseDTOTest.getId(), test);
        assertTrue(EqualDTOChecker.ifProductEqual(test, generatedProduct));
    }

    @Test
    @Transactional
    void ValidateProductNameTest() throws ValidationException{
        UpdateProductDTO noNameProduct = ExampleDTOBuilder.buildNoNameProductDTO();
        assertThrows(ValidationException.class, () -> {
            Validator.validate(noNameProduct);
        });
    }

    @Test
    @Transactional
    void ValidateProductDescriptionTest() throws ValidationException{
        UpdateProductDTO noDescriptionProduct = ExampleDTOBuilder.buildNoDescriptionProductDTO();
        assertThrows(ValidationException.class, () -> {
            Validator.validate(noDescriptionProduct);
        });
    }

    @Test
    @Transactional
    void ValidateProductPriceNotNullTest() throws ValidationException{
        UpdateProductDTO noPriceProduct = ExampleDTOBuilder.buildNoPriceProductDTO();
        assertThrows(ValidationException.class, () -> {
            Validator.validate(noPriceProduct);
        });
    }

    @Test
    @Transactional
    void ValidateProductPriceTest() throws ValidationException{
        UpdateProductDTO negativeStockProduct = ExampleDTOBuilder.buildNegativeStockProductDTO();
        assertThrows(ValidationException.class, () -> {
            Validator.validate(negativeStockProduct);
        });
    }
    @Test
    @Transactional
    @WithMockUser(username="admin")
    void UpdateRemovedProductTest() throws ValidationException{
        UpdateProductDTO test = ExampleDTOBuilder.buildExampleProductDTO();
        CreateWarehouseDTO warehouseTest = ExampleDTOBuilder.buildExampleWarehouseDTO();
        WarehouseDTO warehouseDTOTest = wareService.createWarehouse(warehouseTest);
        ProductDTO generatedProduct = prodService.createProduct(warehouseDTOTest.getId(), test);
        prodService.deleteProduct(generatedProduct.getId());
        assertThrows(ValidationException.class, () -> {
            prodService.updateProduct(generatedProduct.getId(), test);
        });
    }

    @Test
    @Transactional
    @WithMockUser(username="admin")
    void UpdateProductTest() throws ValidationException{
        UpdateProductDTO test = ExampleDTOBuilder.buildExampleProductDTO();
        CreateWarehouseDTO warehouseTest = ExampleDTOBuilder.buildExampleWarehouseDTO();
        WarehouseDTO warehouseDTOTest = wareService.createWarehouse(warehouseTest);
        ProductDTO generatedProduct = prodService.createProduct(warehouseDTOTest.getId(), test);
        ProductDTO updatedProduct = prodService.updateProduct(generatedProduct.getId(), UpdateProductDTO.builder()
        .name("Produkt2")
        .description("Produkt testowy")
        .price(1.11F)
        .build());
        assertTrue(!EqualDTOChecker.ifProductEqual(test, updatedProduct));

    }

    @Test
    @Transactional
    @WithMockUser(username="admin")
    void CreateProductaGetListWithPlusOneProductTest() throws ValidationException{
        int preSize = prodService.getProducts(0, 100).getItems().size();
        UpdateProductDTO test = ExampleDTOBuilder.buildExampleProductDTO();
        CreateWarehouseDTO warehouseTest = ExampleDTOBuilder.buildExampleWarehouseDTO();
        WarehouseDTO warehouseDTOTest = wareService.createWarehouse(warehouseTest);
        prodService.createProduct(warehouseDTOTest.getId(), test);
        assertTrue(prodService.getProducts(0, 100).getItems().size() == preSize+1);

    }

    @Test
    @Transactional
    @WithMockUser(username="admin")
    void CreateProductaDeleteGetListProductTest(){
        UpdateProductDTO test = ExampleDTOBuilder.buildExampleProductDTO();
        CreateWarehouseDTO warehouseTest = ExampleDTOBuilder.buildExampleWarehouseDTO();
        WarehouseDTO warehouseDTOTest = wareService.createWarehouse(warehouseTest);
        ProductDTO productCreated = prodService.createProduct(warehouseDTOTest.getId(), test);
        prodService.deleteProduct(productCreated.getId());
        assertTrue(prodService.getProducts(0, 100).getItems()
        .stream()
        .filter(product -> product.getId() == productCreated.getId())
        .collect(Collectors.toList())
        .isEmpty());

    }

    @Test
    @Transactional
    @WithMockUser(username="admin")
    void DeleteDeletedProductTest() throws ValidationException{
        UpdateProductDTO test = ExampleDTOBuilder.buildExampleProductDTO();
        CreateWarehouseDTO warehouseTest = ExampleDTOBuilder.buildExampleWarehouseDTO();
        WarehouseDTO warehouseDTOTest = wareService.createWarehouse(warehouseTest);
        ProductDTO generatedProduct = prodService.createProduct(warehouseDTOTest.getId(), test);
        prodService.deleteProduct(generatedProduct.getId());
        assertThrows(ValidationException.class, () -> {
            prodService.deleteProduct(generatedProduct.getId());
        });
    }
}
