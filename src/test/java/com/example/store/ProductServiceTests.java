package com.example.store;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    private ProductService prodserv;
    @Autowired
    private WarehouseService wareserv;

    @Test
    @Transactional
    void getNonExistingProductByIdTest() throws NotFoundException{
        Long id = -1L;
        assertThrows(NotFoundException.class, () -> {
            prodserv.getProduct(id);
        });
    }

    @Test
    @Transactional
    @WithMockUser(username="admin")
    void CreateProductTest(){
        UpdateProductDTO test = ExampleDTOBuilder.buildExampleProductDTO();
        CreateWarehouseDTO warehouseTest = ExampleDTOBuilder.buildExampleWarehouseDTO();
        WarehouseDTO warehouseDTOTest = wareserv.createWarehouse(warehouseTest);
        ProductDTO generatedProduct = prodserv.createProduct(warehouseDTOTest.getId(), test);
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
        WarehouseDTO warehouseDTOTest = wareserv.createWarehouse(warehouseTest);
        ProductDTO generatedProduct = prodserv.createProduct(warehouseDTOTest.getId(), test);
        prodserv.deleteProduct(generatedProduct.getId());
        assertThrows(ValidationException.class, () -> {
            prodserv.updateProduct(generatedProduct.getId(), test);
        });
    }
    @Test
    @Transactional
    @WithMockUser(username="admin")
    void DeleteDeletedProductTest() throws ValidationException{
        UpdateProductDTO test = ExampleDTOBuilder.buildExampleProductDTO();
        CreateWarehouseDTO warehouseTest = ExampleDTOBuilder.buildExampleWarehouseDTO();
        WarehouseDTO warehouseDTOTest = wareserv.createWarehouse(warehouseTest);
        ProductDTO generatedProduct = prodserv.createProduct(warehouseDTOTest.getId(), test);
        prodserv.deleteProduct(generatedProduct.getId());
        assertThrows(ValidationException.class, () -> {
            prodserv.deleteProduct(generatedProduct.getId());
        });
    }
}
