package com.thewildoasis.modules.cabins.service;

import com.thewildoasis.dto.ResponseDto;
import com.thewildoasis.entities.Cabin;
import com.thewildoasis.exception.CabinNotFoundException;
import com.thewildoasis.exception.GlobalAppException;
import com.thewildoasis.exception.ResourceNotFoundException;
import com.thewildoasis.modules.cabins.repository.ICabinsRepository;
import com.thewildoasis.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CabinsServiceImpl implements ICabinsService {

    private final ICabinsRepository cabinsRepository;
    private final S3Service s3Service;

    @Value("${aws.s3.bucket}")
    private String s3Bucket;

    @Override
    public List<Cabin> getAllCabins() {
        return cabinsRepository.findAll();
    }

    @Override
    public ResponseDto deleteCabinById(Integer id) {
        Cabin cabin = cabinsRepository.findById(id).orElseThrow(() -> new CabinNotFoundException("Cabin not found to delete."));
        s3Service.deleteObject(s3Bucket, "cabin-images/%s/%s".formatted(id, cabin.getImage()));
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
        cabinsRepository.findAll().forEach(cabin -> s3Service.deleteObject(s3Bucket, "cabin-images/%s/%s".formatted(cabin.getId(), cabin.getImage())));
        cabinsRepository.deleteAll();
        return new ResponseDto(1, "Cabins deleted successfully");
    }

    @Override
    public List<Cabin> insertAll(List<Cabin> cabins) {
        return cabinsRepository.saveAll(cabins);
    }

    @Override
    public List<Integer> findCabinIds() {
        return cabinsRepository.findCabinIds();
    }

    @Override
    public ResponseDto uploadCabinImage(Integer id, MultipartFile file) {
        Cabin cabin = cabinsRepository.findById(id).orElseThrow(() ->
                new CabinNotFoundException("Cabin not found to upload image."));
        String cabinImageId = cabin.getImage() == null || cabin.getImage().isEmpty() ?
                UUID.randomUUID().toString() : cabin.getImage();
        try {
            s3Service.putObject(s3Bucket,
                    "cabin-images/%s/%s".formatted(id, cabinImageId),
                    file.getBytes());
        } catch (Exception e) {
            throw new GlobalAppException("Failed to upload cabin image to s3", e);
        }
        cabin.setImage(cabinImageId);
        cabinsRepository.save(cabin);
        return new ResponseDto(1, "Cabin image uploaded successfully");
    }

    @Override
    public byte[] getCabinImage(Integer id) {
        Cabin cabin = cabinsRepository.findById(id).orElseThrow(() ->
                new CabinNotFoundException("Cabin not found to download image."));
        if (cabin.getImage() == null || cabin.getImage().isEmpty()) {
            throw new ResourceNotFoundException(
                    "Cabin with id [%s] has no image".formatted(id));
        }
        return s3Service.getObject(s3Bucket, "cabin-images/%s/%s".formatted(id, cabin.getImage()));
    }
}
