package com.example.store.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.store.dto.payu.PayuBuyerDTO;
import com.example.store.dto.payu.PayuCreateOrderDTO;
import com.example.store.dto.payu.PayuCreateOrderResponseDTO;
import com.example.store.dto.payu.PayuProductDTO;
import com.example.store.dto.payu.PayuSignInResponseDTO;
import com.example.store.dto.payu.PayuStatusOrderResponseDTO;
import com.example.store.dto.payu.enums.PayuOrderStatus;
import com.example.store.entity.OrderEntity;
import com.example.store.entity.UserEntity;


@Service
public class PayuService {

    private static final String createOrderURL = "https://secure.snd.payu.com/api/v2_1/orders";
    private static final String signInURL = "https://secure.snd.payu.com/pl/standard/user/oauth/authorize";
    private static final String getOrderURL = "https://secure.snd.payu.com//api/v2_1/orders/";

    private final UserService userService;
    private RestTemplate restTemplate;

    @Value("${payu.post_id}")
    private String postId;

    @Value("${payu.second_key}")
    private String secondKey;

    @Value("${payu.oauth.client_id}")
    private String clientId;

    @Value("${payu.oauth.client_secret}")
    private String clientSecret;

    private String accessToken;


    public PayuService(UserService userService) {
        this.userService = userService;
        this.restTemplate = new RestTemplate();
        this.accessToken = "";
    }


    /**
     * sets PayU redirect URL in order entity
     */
    public void sendOrder(OrderEntity order) {
        PayuCreateOrderDTO body = createPayuOrder(order);
        ResponseEntity<PayuCreateOrderResponseDTO> response;

        try {
            response = sendOrder(body);
            order.setPayuRedirectURL(response.getBody().getRedirectUri());
            order.setPayuOrderId(response.getBody().getOrderId());
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
                signIn();
                response = sendOrder(body);
                System.out.println(response.getBody().getRedirectUri());
                order.setPayuRedirectURL(response.getBody().getRedirectUri());
                order.setPayuOrderId(response.getBody().getOrderId());
            }
            else 
                e.printStackTrace();
        }
    }
    
    private ResponseEntity<PayuCreateOrderResponseDTO> sendOrder(PayuCreateOrderDTO body) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        var entity = new HttpEntity<PayuCreateOrderDTO>(body, headers);
        return restTemplate.postForEntity(createOrderURL, entity, PayuCreateOrderResponseDTO.class);
    }


    private void signIn() {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = String.format(
            "grant_type=%s&client_id=%s&client_secret=%s",
            "client_credentials",
            clientId,
            clientSecret
        );
        
        var entity = new HttpEntity<String>(body, headers);
        ResponseEntity<PayuSignInResponseDTO> response = restTemplate.postForEntity(
                signInURL, entity, PayuSignInResponseDTO.class);
        
        accessToken = response.getBody().getAccess_token();
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
                .customerIp("127.0.0.1")
                .merchantPosId(clientId)
                .description("Don't seek for easter eggs here")
                .currencyCode("PLN")
                .totalAmount(priceToString(sum))
                .extOrderId(Long.toString(order.getId()))
                .buyer(PayuBuyerDTO.builder()
                        .email(user.getEmail())
                        .phone(order.getAddress().getPhone())
                        .firstName(user.getUsername())
                        .lastName(user.getUsername())
                        .language("pl")
                        .build())
                .products(products)
                .build();
    }
    
    private String priceToString(float price) {
        return Integer.toString((int) (price * 100));
    }


    public PayuOrderStatus getPaymentStatus(OrderEntity order){
        PayuOrderStatus status = null;
        try {
            status = getStatus(order);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
                signIn();
                status = getStatus(order);
            }
            else{
                e.printStackTrace();
            }
        }
        return status;
    }

    private PayuOrderStatus getStatus(OrderEntity order) {
        var headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        var entity = new HttpEntity<PayuStatusOrderResponseDTO>(null, headers);
        var response = restTemplate.exchange(
            getOrderURL + order.getPayuOrderId(),
            HttpMethod.GET,
            entity,
            PayuStatusOrderResponseDTO.class
        );
        return response.getBody().getOrders().get(0).getStatus();
    }
}
