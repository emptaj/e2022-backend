package com.example.store.dto.payu;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayuBuyerDTO {
    private String email;
    private String phone;
    private String firstName;
    private String lastName;
    private String language;
}
