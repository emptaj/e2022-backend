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
public class NotificationDTO {
    private NotificationOrderDTO order;
    private String localReceiptDateTime;
    private List<NotificationPropertiesDTO> properties;
}
