package ru.andryss.rutube.service;

import io.minio.BucketExistsArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.http.Method;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.andryss.rutube.exception.StorageException;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class SourceServiceImpl implements SourceService {

    @Value("${storage.bucket-name}")
    private String bucketName;

    @Value("${storage.url.expiration-time-ms}")
    private int linkExpirationMs;

    private final UploadingService uploadingService;
    private final MinioClient minioClient;

    @PostConstruct
    public void createBucket() {
        try {
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (Exception e) {
            throw new StorageException(e);
        }
    }

    @Override
    public String generateUploadLink(String sourceId) {
        try {
            String url = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(bucketName)
                            .object(sourceId)
                            .expiry(linkExpirationMs, TimeUnit.MILLISECONDS)
                            .method(Method.POST)
                            .build());
            // TODO: validate
            uploadingService.validateUploading(sourceId);
            return url;
        } catch (Exception e) {
            throw new StorageException(e);
        }
    }

    @Override
    public String generateDownloadLink(String sourceId) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(bucketName)
                            .object(sourceId)
                            .expiry(linkExpirationMs, TimeUnit.MILLISECONDS)
                            .method(Method.GET)
                            .build());
        } catch (Exception e) {
            throw new StorageException(e);
        }
    }
}
