package com.example.store.entity.enums;

public enum OrderState {
    NEW,
    ACCEPTED, // Zamówienie opłacone przez system PayU
    REJECTED,
    CANCELLED,
    SENT,
    DELIVERED
}
