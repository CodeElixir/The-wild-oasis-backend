package com.thewildoasis.modules.bookings.mapper;

import com.thewildoasis.modules.bookings.dto.BookingDto;
import com.thewildoasis.modules.bookings.dto.CabinDto;
import com.thewildoasis.modules.bookings.dto.GuestDto;

import java.time.LocalDateTime;

public class BookingsMapper {
    public static BookingDto maptOBookingDto(Object[] arr) {
        return BookingDto.builder()
                .id((Integer) arr[0])
                .createdAt((LocalDateTime) arr[1])
                .startDate((LocalDateTime) arr[2])
                .endDate((LocalDateTime) arr[3])
                .numNights((Integer) arr[4])
                .numGuests((Integer) arr[5])
                .status((String) arr[6])
                .totalPrice((Double) arr[7])
                .cabins(new CabinDto((String) arr[8]))
                .guests(new GuestDto((String) arr[9], (String) arr[10]))
                .build();
    }
}
