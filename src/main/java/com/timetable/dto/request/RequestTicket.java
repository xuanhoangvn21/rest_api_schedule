package com.timetable.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;

import com.timetable.entities.ScheduleEntity;

@Data
public class RequestTicket {
    private Long amount;
    private String description;
    @Pattern(regexp = "^(VNPay|Paypal)$", message = "Invalid payment method. Allowed values are 'VNPay' or 'Paypal'")
    private String paymentMethod;
    private Long MovieId;
}
