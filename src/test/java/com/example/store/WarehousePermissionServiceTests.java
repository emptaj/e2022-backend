package com.example.store;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import com.example.store.Builder.ExampleDTOBuilder;
import com.example.store.dto.warehouse.WarehouseDTO;
import com.example.store.entity.enums.WarehousePermission;
import com.example.store.exception.NotFoundException;
import com.example.store.service.UserRegistrationService;
import com.example.store.service.WarehousePermissionService;
import com.example.store.service.WarehouseService;

@SpringBootTest
public class WarehousePermissionServiceTests {
    
    @Autowired
    private WarehouseService warehouseService;
    @Autowired
    private WarehousePermissionService warehousePermissionService;
    @Autowired
    private UserRegistrationService service;

    @Test
    @Transactional
    @WithMockUser(username="admin")
    void CreatePermisionForExistingWarehouseTest(){
        WarehouseDTO warehouse = warehouseService.createWarehouse(ExampleDTOBuilder.buildExampleWarehouseDTO());
        assertTrue(!warehousePermissionService.getForWarehouse(warehouse.getId()).isEmpty());
        
    }

    @Test
    @Transactional
    void CreatePermisionForNONExistingWarehouseTest() throws NotFoundException{
        
        assertThrows(NotFoundException.class, () -> {
            warehousePermissionService.getForWarehouse(-1L);
        });    

        
    }

    @Test
    @Transactional
    @WithMockUser(username="admin")
    void CreatePermisionAndAssignToUserTest(){
        WarehouseDTO warehouse = warehouseService.createWarehouse(ExampleDTOBuilder.buildExampleWarehouseDTO());
        
        //warehousePermissionService.assignPermissionToWarehouse(warehouse.getId(), , WarehousePermission.READ;);
    }


}
