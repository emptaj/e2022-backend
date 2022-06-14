package com.example.store.dto.payu;

import java.util.List;

import com.example.store.dto.payu.enums.PayuOrderStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayuOrderDTO {
    private String orderId; 
    private String extOrderId; 
    private String orderCreateDate; 
    private String notifyUrl; 
    private String customerIp; 
    private String merchantPosId; 
    private String description; 
    private String currencyCode; 
    private String totalAmount;
    private PayuBuyerDTO buyer;
    private PayuPayMethod payMethod;
    private List<PayuProductDTO> products; 
    private PayuOrderStatus status;
}