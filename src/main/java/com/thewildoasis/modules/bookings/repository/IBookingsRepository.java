package com.thewildoasis.modules.bookings.repository;

import com.thewildoasis.entities.Booking;
import com.thewildoasis.modules.bookings.projections.BookingPriceResultView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface IBookingsRepository extends JpaRepository<Booking, Integer> {

    @Query("select b.id, b.createdAt, b.startDate, b.endDate, b.numNights, b.numGuests, " +
            "b.status, b.totalPrice, c.name, g.fullName, g.email " +
            "from Booking b inner join b.cabin c " +
            "inner join b.guest g " +
            "where b.status = :status")
    Page<Object[]> findBookingsByStatusOrderBy(Pageable pageable, String status);

    @Query("select b.id, b.createdAt, b.startDate, b.endDate, b.numNights, b.numGuests, " +
            "b.status, b.totalPrice, c.name, g.fullName, g.email " +
            "from Booking b inner join b.cabin c " +
            "inner join b.guest g ")
    Page<Object[]> findBookingsOrderBy(Pageable pageable);

    Booking findBookingById(Integer id);

    List<BookingPriceResultView> getBookingsByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Booking> getBookingsByStartDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("select b from Booking b where (b.status = 'unconfirmed' and b.startDate = :startDate) " +
            "or (b.status = 'checked-in' and b.endDate = :endDate) order by b.createdAt")
    List<Booking> getStaysTodayActivity(LocalDateTime startDate, LocalDateTime endDate);


}
