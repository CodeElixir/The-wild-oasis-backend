package com.thewildoasis.modules.bookings.service;

import com.thewildoasis.dto.ResponseDto;
import com.thewildoasis.entities.Booking;
import com.thewildoasis.entities.Cabin;
import com.thewildoasis.entities.Guest;
import com.thewildoasis.exception.BookingNotFoundException;
import com.thewildoasis.modules.bookings.dto.BookingRequestDto;
import com.thewildoasis.modules.bookings.dto.BookingResponseDto;
import com.thewildoasis.modules.bookings.mapper.BookingsMapper;
import com.thewildoasis.modules.bookings.projections.BookingPriceResultView;
import com.thewildoasis.modules.bookings.repository.IBookingsRepository;
import com.thewildoasis.modules.cabins.repository.ICabinsRepository;
import com.thewildoasis.modules.guests.repository.IGuestsRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements IBookingService {

    private final IBookingsRepository bookingsRepository;
    private final ICabinsRepository cabinsRepository;
    private final IGuestsRepository guestsRepository;

    @Override
    public BookingResponseDto getBookings(BookingRequestDto bookingRequest) {
        BookingResponseDto bookingResponse = new BookingResponseDto();
        Sort.Direction direction;
        String directionStr = bookingRequest.getSortDirection();
        List<String> sortProperties = bookingRequest.getSortProperties();
        if (directionStr.equalsIgnoreCase("asc")) {
            direction = Sort.Direction.ASC;
        } else if (directionStr.equalsIgnoreCase("desc")) {
            direction = Sort.Direction.DESC;
        } else {
            direction = Sort.Direction.ASC;
        }
        Sort sort = Sort.by(direction, sortProperties.toArray(new String[0]));
        Pageable pageable = PageRequest.of(bookingRequest.getPageNo() - 1, bookingRequest.getPageSize(), sort);
        Page<Object[]> listPage;
        if (bookingRequest.getStatus() != null) {
            listPage = bookingsRepository.findBookingsByStatusOrderBy(pageable, bookingRequest.getStatus());
        } else {
            listPage = bookingsRepository.findBookingsOrderBy(pageable);
        }
        bookingResponse.setCount(listPage.getTotalElements());
        bookingResponse.setBookings(listPage.getContent().stream().map(BookingsMapper::maptOBookingDto).toList());
        return bookingResponse;
    }

    @Override
    public Booking getBooking(Integer id) {
        return bookingsRepository.findBookingById(id);
    }

    @Override
    public List<BookingPriceResultView> getBookingsAfterDate(LocalDateTime localDateTime) {
        return bookingsRepository.getBookingsByCreatedAtBetween(localDateTime, LocalDateTime.now());
    }

    @Override
    public List<Booking> getStaysAfterDate(LocalDateTime localDateTime) {
        return bookingsRepository.getBookingsByStartDateBetween(localDateTime, LocalDateTime.now());
    }

    @Override
    public List<Booking> getStaysTodayActivity(LocalDateTime startDate, LocalDateTime endDate) {
        return bookingsRepository.getStaysTodayActivity(startDate, endDate);
    }

    @Override
    public Booking updateBooking(Booking booking) {
        bookingsRepository.findById(booking.getId()).
                orElseThrow(() -> new BookingNotFoundException("Booking not found to update."));
        return bookingsRepository.save(booking);
    }

    @Override
    public ResponseDto deleteBooking(Integer id) {
        bookingsRepository.findById(id).orElseThrow(() -> new BookingNotFoundException("Booking not found to delete."));
        bookingsRepository.deleteById(id);
        return new ResponseDto(1, "Booking successfully deleted!");
    }

    @Override
    public ResponseDto deleteAll() {
        bookingsRepository.deleteAll();
        return new ResponseDto(1, "Bookings deleted successfully");
    }

    @Override
    public ResponseDto insertAll(List<Booking> bookings) {
        Cabin[] cabins = cabinsRepository.findAll().toArray(new Cabin[0]);
        Guest[] guests = guestsRepository.findAll().toArray(new Guest[0]);
        bookings = bookings.stream()
                .peek(booking -> {
                    Cabin cabin = cabins[booking.getCabin().getId() - 1];
                    Guest guest = guests[booking.getGuest().getId() - 1];
                    booking.setCabin(cabin);
                    booking.setGuest(guest);
                }).collect(Collectors.toList());
        bookingsRepository.saveAll(bookings);
        return new ResponseDto(1, "Bookings inserted successfully");
    }
}
