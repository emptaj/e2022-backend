package com.example.store.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.store.dto.payu.PayuBuyerDTO;
import com.example.store.dto.payu.PayuCreateOrderDTO;
import com.example.store.dto.payu.PayuProductDTO;
import com.example.store.entity.OrderEntity;
import com.example.store.entity.UserEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PayuService {

    private final UserService userService;


    public void sendOrder(OrderEntity order) {
        PayuCreateOrderDTO body = createPayuOrder(order);
    }
    
    private PayuCreateOrderDTO createPayuOrder(OrderEntity order) {
        float sum = 0.0f;
        for (var detail : order.getOrderDetails())
            sum += detail.getUnitPrice() * detail.getQuantity();

        UserEntity user = userService.getLoggedUserEntity();
        
        List<PayuProductDTO> products = order.getOrderDetails().stream()
                .map((detail) -> new PayuProductDTO(
                    detail.getProduct().getName(), 
                    priceToString(detail.getProduct().getPrice()),
                    Integer.toString(detail.getQuantity())
                ))
                .collect(Collectors.toList());
        return PayuCreateOrderDTO.builder()
                .notifyUrl("http://localhost:8080/orders/notify-payment")
                .customerIp("") // TODO: SET
                .merchantPosId("") // TODO: SET
                .description("Don't seek for easter eggs here")
                .currencyCode("PLN")
                .totalAmount(priceToString(sum))
                .extOrderId(Long.toString(order.getId()))
                .buyer(PayuBuyerDTO.builder()
                        .email(user.getEmail())
                        .phone(order.getAddress().getPhone())
                        .firstName(user.getUsername())
                        .lastName("")
                        .language("pl")
                        .build())
                .products(products)
                .build();
    }
    
    private String priceToString(float price) {
        return Integer.toString((int) (price * 100));
    }
}
