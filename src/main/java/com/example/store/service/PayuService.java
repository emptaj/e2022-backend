package com.example.store.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.store.dto.payu.PayuBuyerDTO;
import com.example.store.dto.payu.PayuCreateOrderDTO;
import com.example.store.dto.payu.PayuCreateOrderResponseDTO;
import com.example.store.dto.payu.PayuProductDTO;
import com.example.store.entity.OrderEntity;
import com.example.store.entity.UserEntity;


@Service
public class PayuService {

    private final UserService userService;
    private RestTemplate restTemplate;

    private static final String createOrderURL = "https://payu.pl"; // TODO: SET


    public PayuService(UserService userService) {
        this.userService = userService;
        this.restTemplate = new RestTemplate();
    }


    public void sendOrder(OrderEntity order) {
        PayuCreateOrderDTO body = createPayuOrder(order);

        ResponseEntity<PayuCreateOrderResponseDTO> response = restTemplate.postForEntity(
                createOrderURL, body, PayuCreateOrderResponseDTO.class);
        if (!response.getBody().getStatus().getStatus().equals("SUCCESS"))
            System.err.println("PayU create order Failed");
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
