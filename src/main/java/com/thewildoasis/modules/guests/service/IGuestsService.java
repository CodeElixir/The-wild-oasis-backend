package com.thewildoasis.modules.guests.service;

import com.thewildoasis.dto.ResponseDto;
import com.thewildoasis.entities.Guest;

import java.util.List;

public interface IGuestsService {
    ResponseDto deleteAll();

    ResponseDto insertAll(List<Guest> guests);

    List<Integer> findGuestIds();

}
