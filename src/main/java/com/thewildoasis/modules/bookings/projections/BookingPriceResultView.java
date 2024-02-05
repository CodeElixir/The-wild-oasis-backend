package com.thewildoasis.modules.bookings.projections;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public interface BookingPriceResultView {
    @JsonProperty("created_at")
    LocalDateTime getCreatedAt();

    Double getExtrasPrice();

    Double getTotalPrice();
}
