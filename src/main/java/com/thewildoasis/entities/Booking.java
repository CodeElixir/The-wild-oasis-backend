package com.thewildoasis.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Bookings")
public class Booking extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull(message = "StartDate should not be null.")
    @Column(name = "startDate", nullable = false)
    private LocalDateTime startDate;

    @NotNull(message = "EndDate should not be null.")
    @Column(name = "endDate", nullable = false)
    private LocalDateTime endDate;

    @JsonProperty("created_at")
    @Column(name = "createdAt", updatable = false)
    private LocalDateTime createdAt;

    @NotNull(message = "Number of nights should not be null.")
    @Positive(message = "Number of nights should not be negative or zero.")
    @Column(name = "numNights", nullable = false)
    private Integer numNights;

    @NotNull(message = "Number of guests should not be null.")
    @Positive(message = "Number of guests should not be negative or zero.")
    @Column(name = "numGuests", nullable = false)
    private Integer numGuests;

    @NotNull(message = "Cabin price should not be null.")
    @Positive(message = "Cabin price should not be negative or zero.")
    @Column(name = "cabinPrice", nullable = false)
    private Double cabinPrice;

    @NotNull(message = "Extras price should not be null.")
    @PositiveOrZero(message = "Extras price should not be negative.")
    @Column(name = "extrasPrice", nullable = false)
    private Double extrasPrice;

    @NotNull(message = "Total price should not be null.")
    @Positive(message = "Total price should not be negative or zero.")
    @Column(name = "totalPrice", nullable = false)
    private Double totalPrice;

    @NotBlank(message = "Status should not be blank.")
    @Column(name = "status", nullable = false)
    private String status;

    @NotNull(message = "Has breakfast should not be null.")
    @Column(name = "hasBreakfast", nullable = false)
    private Boolean hasBreakfast;

    @NotNull(message = "isPaid should not be null.")
    @Column(name = "isPaid", nullable = false)
    private Boolean isPaid;

    @Column(name = "observations")
    private String observations;

    @JsonProperty("guests")
    @ManyToOne(targetEntity = Guest.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "guest_id", referencedColumnName = "id")
    @Fetch(FetchMode.JOIN)
    private Guest guest;

    @JsonProperty("cabins")
    @ManyToOne(targetEntity = Cabin.class,
            cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH, CascadeType.MERGE},
            fetch = FetchType.EAGER)
    @JoinColumn(name = "cabin_id", referencedColumnName = "id")
    @Fetch(FetchMode.JOIN)
    private Cabin cabin;
}
