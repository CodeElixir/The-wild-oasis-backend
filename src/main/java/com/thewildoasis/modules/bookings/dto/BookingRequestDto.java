package com.thewildoasis.modules.bookings.dto;

import lombok.Data;

import java.util.List;

@Data
public class BookingRequestDto {
    private String status;
    private List<String> sortProperties;
    private String sortDirection;
    private Integer pageSize;
    private Integer pageNo;
}
