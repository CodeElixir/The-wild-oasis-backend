package com.thewildoasis.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Settings")
public class Settings extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull(message = "Min booking length must not be null.")
    @Positive(message = "Min booking length should not be negative or zero.")
    @Column(name = "minBookingLength", nullable = false)
    private Integer minBookingLength;

    @NotNull(message = "Max booking length must not be null.")
    @Positive(message = "Max booking length should not be negative or zero.")
    @Column(name = "maxBookingLength", nullable = false)
    private Integer maxBookingLength;

    @NotNull(message = "Max guests per booking must not be null.")
    @Positive(message = "Max guests per booking should not be negative or zero.")
    @Column(name = "maxGuestsPerBooking", nullable = false)
    private Integer maxGuestsPerBooking;

    @NotNull(message = "Breakfast price must not be null.")
    @PositiveOrZero(message = "Breakfast price should not be negative.")
    @Column(name = "breakfastPrice", nullable = false)
    private Double breakfastPrice;
}
