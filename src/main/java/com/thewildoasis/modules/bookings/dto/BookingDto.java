package com.thewildoasis.modules.bookings.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDto {
    private Integer id;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer numNights;
    private Integer numGuests;
    private String status;
    private Double totalPrice;
    private CabinDto cabins;
    private GuestDto guests;
}
