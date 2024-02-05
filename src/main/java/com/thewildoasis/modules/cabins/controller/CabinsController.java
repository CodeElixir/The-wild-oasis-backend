package com.thewildoasis.modules.cabins.controller;

import com.thewildoasis.dto.ResponseDto;
import com.thewildoasis.entities.Cabin;
import com.thewildoasis.modules.cabins.service.ICabinsService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
@Getter
@RequestMapping("/api/v1/cabins")
@Validated
public class CabinsController {

    private final ICabinsService cabinsService;


    @GetMapping("/")
    public ResponseEntity<List<Cabin>> getCabins() {
        return ResponseEntity.ok(getCabinsService().getAllCabins());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseDto> deleteCabin(@PathVariable Integer id) {
        return ResponseEntity.ok(getCabinsService().deleteCabinById(id));
    }

    @PatchMapping("/update")
    public ResponseEntity<Cabin> updateCabin(@Valid @RequestBody Cabin cabin) {
        return ResponseEntity.ok(getCabinsService().save(cabin, true));
    }

    @PostMapping("/save")
    public ResponseEntity<Cabin> saveCabin(@Valid @RequestBody Cabin cabin) {
        return ResponseEntity.ok(getCabinsService().save(cabin, false));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteAll() {
        return ResponseEntity.ok(getCabinsService().deleteAll());
    }

    @PostMapping("/saveAll")
    private ResponseEntity<List<Cabin>> saveAll(@Valid @RequestBody List<Cabin> cabins) {
        return ResponseEntity.ok(getCabinsService().insertAll(cabins));
    }

    @GetMapping("/ids")
    public ResponseEntity<List<Integer>> getCabinIds() {
        return ResponseEntity.ok(getCabinsService().findCabinIds());
    }

    @PostMapping(value = "/uploadImage/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto> uploadCabinImage(@PathVariable Integer id,
                                                        @RequestBody MultipartFile file) {
        return ResponseEntity.ok(cabinsService.uploadCabinImage(id, file));
    }

    @GetMapping(value = "{id}/cabin-image",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getCabinImage(@PathVariable("id") Integer id) {
        return cabinsService.getCabinImage(id);
    }
}
