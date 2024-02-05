package com.thewildoasis.modules.cabins.repository;

import com.thewildoasis.entities.Cabin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ICabinsRepository extends JpaRepository<Cabin, Integer> {
    @Query("select id from Cabin order by id")
    List<Integer> findCabinIds();
}
