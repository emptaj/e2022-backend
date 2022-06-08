package com.example.store;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.transaction.Transactional;

import org.apache.tomcat.websocket.WrappedMessageHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import com.example.store.Builder.ExampleDTOBuilder;
import com.example.store.dto.warehouse.WarehouseDTO;
import com.example.store.entity.UserEntity;
import com.example.store.entity.enums.WarehousePermission;
import com.example.store.exception.NotFoundException;
import com.example.store.service.UserRegistrationService;
import com.example.store.service.UserService;
import com.example.store.service.WarehousePermissionService;
import com.example.store.service.WarehouseService;

@SpringBootTest
public class WarehousePermissionServiceTests {
    
    @Autowired
    private WarehouseService warehouseService;
    @Autowired
    private WarehousePermissionService warehousePermissionService;
    @Autowired
    private UserRegistrationService registrationService;
    @Autowired
    private UserService userService;

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
        userService.loadUserByUsername("admin");
        WarehouseDTO warehouse = warehouseService.createWarehouse(ExampleDTOBuilder.buildExampleWarehouseDTO());
        warehouseService.deleteWarehouse(warehouse.getId());

        assertThrows(NotFoundException.class, () -> {
            warehousePermissionService.getForWarehouse(warehouse.getId());
        });    
    }


}
