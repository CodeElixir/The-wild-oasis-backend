package com.thewildoasis.modules.bookings.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GuestDto {
    private String email;
    private String fullName;
}
