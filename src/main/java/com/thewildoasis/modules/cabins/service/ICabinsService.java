package com.thewildoasis.modules.cabins.service;

import com.thewildoasis.dto.ResponseDto;
import com.thewildoasis.entities.Cabin;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ICabinsService {

    List<Cabin> getAllCabins();

    ResponseDto deleteCabinById(Integer id);

    Cabin save(Cabin saveCabin, boolean isUpdate);

    ResponseDto deleteAll();

    List<Cabin> insertAll(List<Cabin> cabins);

    List<Integer> findCabinIds();

    ResponseDto uploadCabinImage(Integer id, MultipartFile file);

    byte[] getCabinImage(Integer id);
}
