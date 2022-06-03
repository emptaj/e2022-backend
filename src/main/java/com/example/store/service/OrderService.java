package com.example.store.service;

import com.example.store.dto.ListDTO;
import com.example.store.dto.order.CreateOrderDTO;
import com.example.store.dto.order.CreateOrderDetailsDTO;
import com.example.store.dto.order.OrderDTO;
import com.example.store.dto.order.OrderDetailsDTO;
import com.example.store.entity.AddressEntity;
import com.example.store.entity.DeliveryTypeEntity;
import com.example.store.entity.OrderDetailsEntity;
import com.example.store.entity.OrderEntity;
import com.example.store.entity.ProductEntity;
import com.example.store.entity.UserEntity;
import com.example.store.entity.WarehouseEntity;
import com.example.store.entity.enums.OrderState;
import com.example.store.exception.NotFoundException;
import com.example.store.exception.ValidationException;
import com.example.store.mapper.OrderMapper;
import com.example.store.repository.OrderDetailsRepository;
import com.example.store.repository.OrderRepository;
import com.example.store.repository.ProductRepository;
import com.example.store.repository.UserRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
    
    private final OrderMapper mapper = OrderMapper.INSTANCE;

    private final OrderRepository repository;
    private final OrderDetailsRepository detailsRepository;
    private final ProductRepository productRepository;

    private final ProductService productService;
    private final AddressService addressService;
    private final DeliveryTypeService deliveryService;

    private final UserRepository userRepository; // FIXME: remove this


    public OrderEntity findOrderById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(OrderEntity.class, id));
    }

    public OrderDetailsEntity findOrderDetailsById(Long id) {
        return detailsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(OrderDetailsEntity.class, id));
    }


    public OrderDTO getOrder(Long orderId) {
        OrderEntity entity = findOrderById(orderId);
        List<OrderDetailsDTO> detailsDTO = entity.getOrderDetails().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());

        return mapper.toDTO(entity, detailsDTO);
    }


    public List<OrderDTO> createOrder(CreateOrderDTO dto) {
        AddressEntity address = addressService.createAddressEntity(dto.getAddress());
        DeliveryTypeEntity deliveryType = deliveryService.findDeliveryTypeById(dto.getDeliveryTypeId());
        UserEntity user = new UserEntity(); // FIXME: get current user; DO NOT CREATE NEW ONE
        user = userRepository.save(user);   // FIXME: remove this

        List<OrderDTO> ordersDTOList = new ArrayList<>();

        Map<WarehouseEntity, List<Pair<ProductEntity, Integer>>> ordersMap = splitOrder(dto.getOrderDetails());
        mergeDuplicatedProducts(ordersMap);

        for (Entry<WarehouseEntity, List<Pair<ProductEntity, Integer>>> entry : ordersMap.entrySet()) {
            WarehouseEntity warehouse = entry.getKey();
            List<Pair<ProductEntity, Integer>> items = entry.getValue();

            OrderEntity order = mapper.create(user, LocalDate.now(), warehouse, address, deliveryType);
            order = repository.save(order);

            List<OrderDetailsEntity> orderDetailsList = prepareOrderDetails(items, order);
            order.setOrderDetails(orderDetailsList);

            List<OrderDetailsDTO> orderDetailsDTOList = orderDetailsList.stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
            
            ordersDTOList.add(mapper.toDTO(order, orderDetailsDTOList));
        }

        return ordersDTOList;
    }
    
    private Map<WarehouseEntity, List<Pair<ProductEntity, Integer>>> splitOrder(List<CreateOrderDetailsDTO> orderDetails) {
        var ordersMap = new HashMap<WarehouseEntity, List<Pair<ProductEntity, Integer>>>();
        
        for (CreateOrderDetailsDTO item : orderDetails) {
            ProductEntity product = productService.findProductById(item.getProductId());
            Integer quantity = item.getQuantity();
            validatePositiveQuantity(quantity);

            WarehouseEntity warehouse = product.getWarehouse();
            List<Pair<ProductEntity, Integer>> productList = ordersMap.get(warehouse);
            if (productList == null) {
                productList = new ArrayList<>();
                ordersMap.put(warehouse, productList);
            } 
            productList.add(Pair.of(product, quantity));
        }

        return ordersMap;
    }

    /**
     * Sum up all quantity for duplicated products and leave only unique ones
     * @param ordersMap
     */
    private void mergeDuplicatedProducts(Map<WarehouseEntity, List<Pair<ProductEntity, Integer>>> ordersMap) {
        
        for (var itemsList : ordersMap.values()) {

            var itemsMap = new HashMap<Long, Pair<ProductEntity, Integer>>();

            for (var item : itemsList) {
                ProductEntity product = item.getFirst();
                Integer quantity = item.getSecond();

                var mapItem = itemsMap.get(product.getId());
                if (mapItem != null)
                    quantity += mapItem.getSecond();
                itemsMap.put(product.getId(), Pair.of(product, quantity));
            }
            
            itemsList.clear();
            itemsList.addAll(itemsMap.values());
        }
    }

    private List<OrderDetailsEntity> prepareOrderDetails(List<Pair<ProductEntity, Integer>> items, OrderEntity order) {
        var orderDetailsList = new ArrayList<OrderDetailsEntity>();

        for (var item : items) {
            ProductEntity product = item.getFirst();
            Integer quantity = item.getSecond();
            
            product.setUnitsInOrder(product.getUnitsInOrder() + quantity);
            product = productRepository.save(product);

            OrderDetailsEntity orderDetails = mapper.create(order, product, quantity);
            orderDetails = detailsRepository.save(orderDetails);
            orderDetailsList.add(orderDetails);
        }

        return orderDetailsList;
    }
    
    private void validatePositiveQuantity(Integer number) {
        if (number <= 0)
            throw new ValidationException("Product quantity must be positive");
    }

    
    public ListDTO<OrderDTO> getUserOrders(Long userId, int page, int size) {
        UserEntity user = UserEntity.builder().id(userId).build(); // FIXME: find user
        userRepository.save(user);                      // FIXME: remove this
        Page<OrderEntity> pageResponse = repository.findAllByUserIdAndStateNotIn(
                user, List.of(OrderState.CANCELLED, OrderState.DELIVERED, OrderState.REJECTED),
                PageRequest.of(page, size));
        
        var result = new ArrayList<OrderDTO>();
        for (var entity : pageResponse.getContent()) {
            List<OrderDetailsDTO> details = entity.getOrderDetails().stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());

            result.add(mapper.toDTO(entity, details));
        }
        
        return new ListDTO<>(pageResponse.getTotalPages(), result);
    }
}
