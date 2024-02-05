package com.thewildoasis.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
@Table(name = "Cabins")
public class Cabin extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotBlank(message = "Cabin name must not be blank.")
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull(message = "Max Capacity must not be null.")
    @Positive(message = "Max Capacity should not be negative or zero.")
    @Column(name = "maxCapacity", nullable = false)
    private Integer maxCapacity;

    @NotNull(message = "Regular price must not be null.")
    @Positive(message = "Regular price should not be negative or zero.")
    @Column(name = "regularPrice", nullable = false)
    private Double regularPrice;

    @NotNull(message = "Discount must not be null.")
    @PositiveOrZero(message = "Discount should not be negative.")
    @Column(name = "discount")
    private Double discount;

    @NotBlank(message = "Description must not be blank.")
    @Column(name = "description", nullable = false, length = 1000)
    private String description;

    @NotBlank(message = "Image must not be blank.")
    @Column(name = "image", nullable = false)
    private String image;

//    @JsonIgnore
//    @OneToMany(targetEntity = Booking.class, mappedBy = "cabin",
//            cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH, CascadeType.MERGE},
//            fetch = FetchType.LAZY)
//    @Fetch(FetchMode.JOIN)
//    private Set<Booking> bookings;
}
