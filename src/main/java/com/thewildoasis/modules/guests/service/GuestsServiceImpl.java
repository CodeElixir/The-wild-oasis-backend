package com.thewildoasis.modules.guests.service;

import com.thewildoasis.dto.ResponseDto;
import com.thewildoasis.entities.Guest;
import com.thewildoasis.modules.guests.repository.IGuestsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GuestsServiceImpl implements IGuestsService {

    private IGuestsRepository guestsRepository;

    @Override
    public ResponseDto deleteAll() {
        guestsRepository.deleteAll();
        return new ResponseDto(1, "Guests deleted successfully");
    }

    @Override
    public ResponseDto insertAll(List<Guest> guests) {
        guestsRepository.saveAll(guests);
        return new ResponseDto(1, "Guests inserted successfully");
    }

    @Override
    public List<Integer> findGuestIds() {
        return guestsRepository.findGuestIds();
    }
}
