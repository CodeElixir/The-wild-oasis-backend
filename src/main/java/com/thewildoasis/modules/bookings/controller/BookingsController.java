package com.thewildoasis.modules.bookings.controller;

import com.thewildoasis.dto.ResponseDto;
import com.thewildoasis.entities.Booking;
import com.thewildoasis.modules.bookings.dto.BookingRequestDto;
import com.thewildoasis.modules.bookings.dto.BookingResponseDto;
import com.thewildoasis.modules.bookings.projections.BookingPriceResultView;
import com.thewildoasis.modules.bookings.service.IBookingService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/bookings")
public class BookingsController {

    private final IBookingService bookingService;

    @PostMapping("/")
    public ResponseEntity<BookingResponseDto> getBookings(@RequestBody BookingRequestDto bookingRequest) {
        return ResponseEntity.ok(bookingService.getBookings(bookingRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBooking(@PathVariable Integer id) {
        return ResponseEntity.ok(bookingService.getBooking(id));
    }

    @GetMapping("/after")
    public ResponseEntity<List<BookingPriceResultView>> getBookingsAfterDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime) {
        return ResponseEntity.ok(bookingService.getBookingsAfterDate(dateTime));
    }

    @GetMapping("/staysAfter")
    public ResponseEntity<List<Booking>> getStaysAfterDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime) {
        return ResponseEntity.ok(bookingService.getStaysAfterDate(dateTime));
    }

    @GetMapping("/staysTodayActivity")
    public ResponseEntity<List<Booking>> getStaysTodayActivity() {
        return ResponseEntity.ok(bookingService.getStaysTodayActivity(
                LocalDateTime.now().with(LocalTime.MIDNIGHT),
                LocalDateTime.now().with(LocalTime.MIDNIGHT)));
    }

    @PostMapping("/update")
    public ResponseEntity<Booking> updateBooking(@RequestBody Booking booking) {
        return ResponseEntity.ok(bookingService.updateBooking(booking));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseDto> deleteBooking(@PathVariable Integer id) {
        return ResponseEntity.ok(bookingService.deleteBooking(id));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteAll() {
        return ResponseEntity.ok(bookingService.deleteAll());
    }

    @PostMapping("/saveAll")
    private ResponseEntity<ResponseDto> saveAll(@RequestBody List<Booking> bookings) {
        return ResponseEntity.ok(bookingService.insertAll(bookings));
    }
}
