package com.example.deliveryapp.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.YearMonth;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardDTO {
    private long id;
    private String cardType;//mastercard sau visa
    private String cardNumber;
    private String cardHolderName;
    private String securityCode;
    private YearMonth expiryDate; //date must be: 04.2043 or 04-2024
    private LocalDate fullExpiryDate;
    private Boolean isDefault;

}
