package com.example.store.dto.payu;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayuSignInResponseDTO {
    private String access_token;
    private String token_type;
    private Integer expires_in;
    private String grant_type;
}
