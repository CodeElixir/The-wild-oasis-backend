package com.thewildoasis.modules.bookings.dto;

import lombok.Data;

import java.util.List;

@Data
public class BookingResponseDto {
    private Long count;
    private List<BookingDto> bookings;
}
