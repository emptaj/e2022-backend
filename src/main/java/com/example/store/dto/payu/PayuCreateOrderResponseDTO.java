package com.example.store.dto.payu;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayuCreateOrderResponseDTO {
    private PayuStatusDTO status;
    private String redirectUri;
    private String orderId;
    private String extOrderId;
}
