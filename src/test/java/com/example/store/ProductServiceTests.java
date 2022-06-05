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
import io.swagger.annotations.Example;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import javax.validation.Validation;

@SpringBootTest
public class ProductServiceTests {

    @Autowired
    private ProductService prodserv;
    @Autowired
    private WarehouseService wareserv;

    @Test
    @Transactional
    void findNonExistingProductByIdTest() throws NotFoundException{
        Long id = -1L;
        assertThrows(NotFoundException.class, () -> {
            prodserv.findProductById(id);
        });
    }
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
    void CreateProductTest(){
        ProductDTO test = ExampleDTOBuilder.buildExampleProductDTO();
        CreateWarehouseDTO warehouseTest = ExampleDTOBuilder.buildExampleWarehouseDTO();
        WarehouseDTO warehouseDTOTest = wareserv.createWarehouse(warehouseTest);
        ProductDTO generatedProduct = prodserv.createProduct(warehouseDTOTest.getId(), test);
        assertTrue(EqualDTOChecker.ifProductEqual(test, generatedProduct));
    }

    @Test
    @Transactional
    void ValidateProductNameTest() throws ValidationException{
        ProductDTO noNameProduct = ExampleDTOBuilder.buildNoNameProductDTO();
        CreateWarehouseDTO warehouseTest = ExampleDTOBuilder.buildExampleWarehouseDTO();
        WarehouseDTO warehouseDTOTest = wareserv.createWarehouse(warehouseTest);
        assertThrows(ValidationException.class, () -> {
            prodserv.createProduct(warehouseDTOTest.getId(), noNameProduct);
        });
    }

    @Test
    @Transactional
    void ValidateProductDescriptionTest() throws ValidationException{
        ProductDTO NoDescriptionProduct = ExampleDTOBuilder.buildNoDescriptionProductDTO();
        CreateWarehouseDTO warehouseTest = ExampleDTOBuilder.buildExampleWarehouseDTO();
        WarehouseDTO warehouseDTOTest = wareserv.createWarehouse(warehouseTest);
        assertThrows(ValidationException.class, () -> {
            prodserv.createProduct(warehouseDTOTest.getId(), NoDescriptionProduct);
        });
    }

    @Test
    @Transactional
    void ValidateProductPriceTest() throws ValidationException{
        ProductDTO NoPriceProduct = ExampleDTOBuilder.buildNoPriceProductDTO();
        CreateWarehouseDTO warehouseTest = ExampleDTOBuilder.buildExampleWarehouseDTO();
        WarehouseDTO warehouseDTOTest = wareserv.createWarehouse(warehouseTest);
        assertThrows(ValidationException.class, () -> {
            prodserv.createProduct(warehouseDTOTest.getId(), NoPriceProduct);
        });
    }

    @Test
    @Transactional
    void ValidateProductPriceTest() throws ValidationException{
        ProductDTO NegativeStockProduct = ExampleDTOBuilder.buildNegativeStockProductDTO();
        CreateWarehouseDTO warehouseTest = ExampleDTOBuilder.buildExampleWarehouseDTO();
        WarehouseDTO warehouseDTOTest = wareserv.createWarehouse(warehouseTest);
        assertThrows(ValidationException.class, () -> {
            prodserv.createProduct(warehouseDTOTest.getId(), NegativeStockProduct);
        });
    }
    @Test
    @Transactional
    void UpdateRemovedProductTest() throws ValidationException{
        ProductDTO test = ExampleDTOBuilder.buildExampleProductDTO();
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
    void DeleteDeletedProductTest() throws ValidationException{
        ProductDTO test = ExampleDTOBuilder.buildExampleProductDTO();
        CreateWarehouseDTO warehouseTest = ExampleDTOBuilder.buildExampleWarehouseDTO();
        WarehouseDTO warehouseDTOTest = wareserv.createWarehouse(warehouseTest);
        ProductDTO generatedProduct = prodserv.createProduct(warehouseDTOTest.getId(), test);
        prodserv.deleteProduct(generatedProduct.getId());
        assertThrows(ValidationException.class, () -> {
            prodserv.deleteProduct(generatedProduct.getId());
        });
    }
}
