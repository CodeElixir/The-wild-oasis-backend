package com.thewildoasis.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Guests")
public class Guest extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotBlank(message = "Full name must not be blank.")
    @Size(min = 3, message = "Full name must be at least 3 characters long.")
    @Column(name = "fullname", nullable = false)
    private String fullName;

    @NotBlank(message = "Email should not be blank.")
    @Email(message = "Please provide valid email address.")
    @Column(name = "email", nullable = false)
    private String email;

    @NotBlank(message = "Nationality must not be blank.")
    @Column(name = "nationality", nullable = false)
    private String nationality;

    @NotBlank(message = "NationalID id must not be blank.")
    @Column(name = "nationalID", nullable = false)
    private String nationalID;

    @NotBlank(message = "CountryFlag url must not be blank.")
    @Column(name = "countryFlag", nullable = false)
    private String countryFlag;

//    @JsonIgnore
//    @OneToMany(targetEntity = Booking.class, mappedBy = "guest", cascade = CascadeType.ALL,
//            orphanRemoval = true, fetch = FetchType.LAZY)
//    @Fetch(FetchMode.JOIN)
//    private Set<Booking> bookings;
}
