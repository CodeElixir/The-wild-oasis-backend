package com.thewildoasis.modules.guests.controller;

import com.thewildoasis.dto.ResponseDto;
import com.thewildoasis.entities.Guest;
import com.thewildoasis.modules.guests.service.IGuestsService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Data
@RequestMapping("/api/v1/guests")
@Validated
public class GuestsController {
    private final IGuestsService guestsService;

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteAll() {
        return ResponseEntity.ok(getGuestsService().deleteAll());
    }

    @PostMapping("/saveAll")
    public ResponseEntity<ResponseDto> saveAll(@Valid @RequestBody List<Guest> guests) {
        return ResponseEntity.ok(getGuestsService().insertAll(guests));
    }

    @GetMapping("/ids")
    public ResponseEntity<List<Integer>> getGuestIds() {
        return ResponseEntity.ok(getGuestsService().findGuestIds());
    }
}
