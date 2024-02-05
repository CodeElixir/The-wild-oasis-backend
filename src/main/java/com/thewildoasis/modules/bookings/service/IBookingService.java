package com.thewildoasis.modules.bookings.service;

import com.thewildoasis.dto.ResponseDto;
import com.thewildoasis.entities.Booking;
import com.thewildoasis.modules.bookings.dto.BookingRequestDto;
import com.thewildoasis.modules.bookings.dto.BookingResponseDto;
import com.thewildoasis.modules.bookings.projections.BookingPriceResultView;

import java.time.LocalDateTime;
import java.util.List;

public interface IBookingService {
    BookingResponseDto getBookings(BookingRequestDto bookingRequest);

    Booking getBooking(Integer id);

    List<BookingPriceResultView> getBookingsAfterDate(LocalDateTime localDateTime);

    List<Booking> getStaysAfterDate(LocalDateTime localDateTime);

    List<Booking> getStaysTodayActivity(LocalDateTime startDate, LocalDateTime endDate);

    Booking updateBooking(Booking booking);

    ResponseDto deleteBooking(Integer id);

    ResponseDto deleteAll();

    ResponseDto insertAll(List<Booking> bookings);

}
