package com.thewildoasis.modules.cabins.service;

import com.thewildoasis.dto.ResponseDto;
import com.thewildoasis.entities.Cabin;
import com.thewildoasis.exception.CabinNotFoundException;
import com.thewildoasis.modules.cabins.repository.ICabinsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CabinsServiceImpl implements ICabinsService {

    private final ICabinsRepository cabinsRepository;

    @Override
    public List<Cabin> getAllCabins() {
        return cabinsRepository.findAll();
    }

    @Override
    public ResponseDto deleteCabinById(Integer id) {
        cabinsRepository.findById(id).orElseThrow(() -> new CabinNotFoundException("Cabin not found to delete."));
        cabinsRepository.deleteById(id);
        return new ResponseDto(1, "Cabin successfully deleted!");
    }

    @Override
    public Cabin save(Cabin saveCabin, boolean isUpdate) {
        if (isUpdate) {
            cabinsRepository.findById(saveCabin.getId()).orElseThrow(() -> new CabinNotFoundException("Cabin not found to delete."));
        }
        return cabinsRepository.save(saveCabin);
    }

    @Override
    public ResponseDto deleteAll() {
        cabinsRepository.deleteAll();
        return new ResponseDto(1, "Cabins deleted successfully");
    }

    @Override
    public ResponseDto insertAll(List<Cabin> cabins) {
        cabinsRepository.saveAll(cabins);
        return new ResponseDto(1, "Cabins inserted successfully");
    }

    @Override
    public List<Integer> findCabinIds() {
        return cabinsRepository.findCabinIds();
    }
}
