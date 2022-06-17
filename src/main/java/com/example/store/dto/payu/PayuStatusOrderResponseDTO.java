package com.example.store.dto.payu;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayuStatusOrderResponseDTO {
    private List<PayuPropertiesDTO> properties;
    private PayuOrderDTO order;
    private PayuDetailStatusDTO status;

}
