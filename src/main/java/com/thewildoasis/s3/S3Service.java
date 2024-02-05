package com.thewildoasis.s3;

import com.thewildoasis.exception.GlobalAppException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

@Slf4j
@Service
@AllArgsConstructor
public class S3Service {
    private final S3Client s3Client;

    public void putObject(String bucketName, String key, byte[] file) {
        try (s3Client) {
            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.putObject(objectRequest, RequestBody.fromBytes(file));
        } catch (Exception e) {
            log.error("An error occurred while putting object from server.", e);
            throw new GlobalAppException(e.getMessage());
        }
    }

    public byte[] getObject(String bucketName, String keyName) {
        try (s3Client) {
            GetObjectRequest objectRequest = GetObjectRequest
                    .builder()
                    .key(keyName)
                    .bucket(bucketName)
                    .build();

            ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(objectRequest);
            return objectBytes.asByteArray();
        } catch (S3Exception e) {
            log.error("An error occurred while fetching object from server.", e);
            throw new GlobalAppException(e.getMessage());
        }
    }

    public void deleteObject(String bucketName, String keyName) {
        try (s3Client) {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
        } catch (S3Exception e) {
            log.error("An error occurred while deleting object from server.", e);
            throw new GlobalAppException(e.getMessage());
        }
    }
}
