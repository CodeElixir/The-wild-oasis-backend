package com.thewildoasis.modules.guests.repository;

import com.thewildoasis.entities.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IGuestsRepository extends JpaRepository<Guest, Integer> {
    @Query("select id from Guest order by id")
    List<Integer> findGuestIds();
}
