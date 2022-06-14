package com.example.store.service;

import static com.example.store.entity.enums.OrderState.NEW;
import static com.example.store.entity.enums.OrderState.ACCEPTED;
import static com.example.store.entity.enums.OrderState.REJECTED;
import static com.example.store.entity.enums.OrderState.CANCELLED;
import static com.example.store.entity.enums.OrderState.SENT;
import static com.example.store.entity.enums.OrderState.DELIVERED;

import com.example.store.dto.ListDTO;
import com.example.store.dto.order.CreateOrderDTO;
import com.example.store.dto.order.CreateOrderDetailsDTO;
import com.example.store.dto.order.OrderDTO;
import com.example.store.dto.payu.PayuNotificationDTO;
import com.example.store.dto.payu.enums.PayuOrderStatus;
import com.example.store.entity.AddressEntity;
import com.example.store.entity.DeliveryTypeEntity;
import com.example.store.entity.OrderDetailsEntity;
import com.example.store.entity.OrderEntity;
import com.example.store.entity.ProductEntity;
import com.example.store.entity.UserEntity;
import com.example.store.entity.WarehouseEntity;
import com.example.store.entity.enums.OrderState;
import com.example.store.entity.enums.WarehousePermission;
import com.example.store.exception.UnauthorizedException;
import com.example.store.exception.ValidationException;
import com.example.store.mapper.OrderMapper;
import com.example.store.repository.DeliveryTypeRepository;
import com.example.store.repository.OrderDetailsRepository;
import com.example.store.repository.OrderRepository;
import com.example.store.repository.ProductRepository;
import com.example.store.repository.UserRepository;
import com.example.store.repository.WarehouseRepository;
import com.example.store.repository.finder.RecordFinder;
import com.example.store.validator.Validator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
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

    private final ProductService productService;
    private final AddressService addressService;
    private final UserService userService;
    private final WarehousePermissionService warehousePermissionService;
    private final PayuService payuService;

    private final RecordFinder<OrderEntity, OrderRepository> finder;
    private final RecordFinder<ProductEntity, ProductRepository> productFinder;
    private final RecordFinder<WarehouseEntity, WarehouseRepository> warehouseFinder;
    private final RecordFinder<DeliveryTypeEntity, DeliveryTypeRepository> deliveryFinder;
    private final RecordFinder<UserEntity, UserRepository> userFinder;


    public OrderDTO getOrder(Long orderId) {
        OrderEntity entity = finder.byId(orderId);
        return mapper.toDTO(entity);
    }


    public List<OrderDTO> createOrder(CreateOrderDTO dto) {
        AddressEntity address = addressService.createAddressEntity(dto.getAddress());
        DeliveryTypeEntity deliveryType = deliveryFinder.byId(dto.getDeliveryTypeId());
        UserEntity user = userService.getLoggedUserEntity();

        Map<WarehouseEntity, List<Pair<ProductEntity, Integer>>> ordersMap = splitOrder(dto.getOrderDetails());
        mergeDuplicatedProducts(ordersMap);

        var ordersDTOList = new ArrayList<OrderDTO>();
        var orderList = new ArrayList<OrderEntity>();
        for (var entry : ordersMap.entrySet()) {
            WarehouseEntity warehouse = entry.getKey();
            List<Pair<ProductEntity, Integer>> items = entry.getValue();

            OrderEntity order = mapper.create(user, LocalDate.now(), warehouse, address, deliveryType);
            order = repository.save(order);

            List<OrderDetailsEntity> orderDetailsList = prepareOrderDetails(items, order);
            order.setOrderDetails(orderDetailsList);

            ordersDTOList.add(mapper.toDTO(order));
            orderList.add(order);
        }

        for (var order : orderList)
            payuService.sendOrder(order);

        return ordersDTOList;
    }

    private Map<WarehouseEntity, List<Pair<ProductEntity, Integer>>> splitOrder(List<CreateOrderDetailsDTO> orderDetails) {
        var ordersMap = new HashMap<WarehouseEntity, List<Pair<ProductEntity, Integer>>>();

        for (CreateOrderDetailsDTO item : orderDetails) {
            ProductEntity product = productFinder.byId(item.getProductId());
            Integer quantity = item.getQuantity();
            Validator.positiveValue(quantity, "Product quantity must be positive");

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
     *
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

        for (Pair<ProductEntity, Integer> item : items) {
            OrderDetailsEntity orderDetails = mapper.create(order, item.getFirst(), item.getSecond());
            orderDetails = detailsRepository.save(orderDetails);
            orderDetailsList.add(orderDetails);
        }

        return orderDetailsList;
    }


    public ListDTO<OrderDTO> getUserOrders(Long userId, int page, int size) {
        UserEntity user = userFinder.byId(userId);
        Page<OrderEntity> pageResponse = repository.findAllByUserIdAndStateNotIn(
                user.getId(), List.of(OrderState.CANCELLED, OrderState.DELIVERED, OrderState.REJECTED),
                PageRequest.of(page, size));

        var result = pageResponse.getContent().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());

        return new ListDTO<>(pageResponse.getTotalPages(), result);
    }

    public OrderDTO changeOrderState(Long orderId, OrderState nextState) {
        OrderEntity order = finder.byId(orderId);
        order = changeOrderState(order, nextState);
        return mapper.toDTO(order);
    }

    private OrderEntity changeOrderState(OrderEntity order, OrderState nextState) {
        validatePermissions(order.getWarehouse(), WarehousePermission.UPDATE);
        validateNextState(order.getState(), nextState);

        return goNextState(order, nextState, userService.getLoggedUserEntity());
    }

    private void validatePermissions(WarehouseEntity warehouse, WarehousePermission permission) {
        if (!warehousePermissionService.hasPermission(userService.getLoggedUserEntity(), warehouse, permission))
            throw new UnauthorizedException("Unauthorized");
    }

    private void validateNextState(OrderState currentState, OrderState nextState) {
        switch (currentState) {
            case NEW:     validateStateIn(nextState, List.of(ACCEPTED, REJECTED)); break;
            case ACCEPTED:validateStateIn(nextState, List.of(CANCELLED, SENT));    break;
            case SENT:    validateStateIn(nextState, List.of(DELIVERED));          break;
            default:      validateStateIn(nextState, Collections.emptyList());
        }
    }

    private void validateStateIn(OrderState state, List<OrderState> orderStates) {
        if (!orderStates.contains(state))
            throw new ValidationException("Cannot change order state to " + state.name());
    }

    private OrderEntity goNextState(OrderEntity order, OrderState nextState, UserEntity user) {
        BiConsumer<ProductEntity, Integer> consumer;

        switch (nextState) {
            case SENT:      consumer = productService::sendProduct;        break;
            case ACCEPTED:  consumer = productService::orderProduct;       break;
            case CANCELLED: consumer = productService::removeProductOrder; break;
            default:        consumer = (product, quantity) -> {};
        }

        for (OrderDetailsEntity details : order.getOrderDetails())
            consumer.accept(details.getProduct(), details.getQuantity());

        order = mapper.changeState(order, nextState, user, LocalDate.now());
        order = repository.save(order);

        return order;
    }

    public ListDTO<OrderDTO> getPendingOrders(Long warehouseId, int page, int size) {
        WarehouseEntity warehouse = warehouseFinder.byId(warehouseId);
        return getPendingOrders(warehouse, page, size);
    }

    public ListDTO<OrderDTO> getPendingOrders(WarehouseEntity warehouse, int page, int size) {
        Page<OrderEntity> pageResponse = repository.findAllByWarehouseIdAndStateIn(
                warehouse.getId(), List.of(NEW, ACCEPTED, SENT), PageRequest.of(page, size));

        var result = pageResponse.getContent().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());

        return new ListDTO<>(pageResponse.getTotalPages(), result);
    }


    public void acceptPayUNotification(PayuNotificationDTO dto) {
        OrderEntity order = finder.byId(Long.parseLong(dto.getOrder().getExtOrderId()));
        PayuOrderStatus payuStatus = dto.getOrder().getStatus();

        switch (payuStatus) {
            case PENDING:                  break;
            case WAITING_FOR_CONFIRMATION: break;
            case COMPLETED:
                if (!order.getState().equals(ACCEPTED))
                    changeOrderState(order, ACCEPTED);
                break;
            case CANCELED:
                changeOrderState(order, CANCELLED);
                break;
            default:
        }
    }
}
