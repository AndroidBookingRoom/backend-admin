package com.example.backend.services.impl;

import com.example.backend.common.CommonUtils;
import com.example.backend.common.MinioUtils;
import com.example.backend.config.MinioConfig;
import com.example.backend.domain.FileResponse;
import com.example.backend.services.MinioService;
import io.minio.messages.Bucket;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MinioServiceImpl implements MinioService {

    private final MinioUtils minioUtil;
    private final MinioConfig minioProperties;


    @Override
    public boolean bucketExists(String bucketName) {
        log.info("MinioServiceImpl | bucketExists is called");

        return minioUtil.bucketExists(bucketName);
    }

    @Override
    public void makeBucket(String bucketName) {
        log.info("MinioServiceImpl | makeBucket is called");

        log.info("MinioServiceImpl | makeBucket | bucketName : {}", bucketName);

        minioUtil.makeBucket(bucketName);
    }

    @Override
    public List<String> listBucketName() {
        log.info("MinioServiceImpl | listBucketName is called");
        return minioUtil.listBucketNames();
    }

    @Override
    public List<Bucket> listBuckets() {
        log.info("MinioServiceImpl | listBuckets is called");
        return minioUtil.listBuckets();
    }

    @Override
    public boolean removeBucket(String bucketName) {
        log.info("MinioServiceImpl | removeBucket is called");

        log.info("MinioServiceImpl | removeBucket | bucketName : {}", bucketName);

        return minioUtil.removeBucket(bucketName);
    }

    @Override
    public List<String> listObjectNames(String bucketName) {
        log.info("MinioServiceImpl | listObjectNames is called");

        log.info("MinioServiceImpl | listObjectNames | bucketName : {}", bucketName);

        return minioUtil.listObjectNames(bucketName);
    }

    @SneakyThrows
    @Override
    public FileResponse putObject(MultipartFile multipartFile, String bucketName, String fileType) {

        log.info("MinioServiceImpl | putObject is called");

        try {
            bucketName = !CommonUtils.isNullOrEmpty(bucketName) ? bucketName : minioProperties.getBucketName();

            log.info("MinioServiceImpl | putObject | bucketName : {}", bucketName);

            if (!this.bucketExists(bucketName)) {
                this.makeBucket(bucketName);
                log.info("MinioServiceImpl | putObject | bucketName : {} created", bucketName);
            }

            String fileName = multipartFile.getOriginalFilename();
            log.info("MinioServiceImpl | getFileType | fileName : {}", fileName);

            Long fileSize = multipartFile.getSize();
            log.info("MinioServiceImpl | getFileType | fileSize : {}", fileSize);

            String objectName = UUID.randomUUID().toString().replaceAll("-", "")
                    + Objects.requireNonNull(fileName).substring(fileName.lastIndexOf("."));
            log.info("MinioServiceImpl | getFileType | objectName : {}", objectName);

            LocalDateTime createdTime = LocalDateTime.now();
            log.info("MinioServiceImpl | getFileType | createdTime : {}", createdTime);

            minioUtil.putObject(bucketName, multipartFile, objectName,fileType);

            log.info("MinioServiceImpl | getFileType | url : {}/{}/{}", minioProperties.getEndpoint(), bucketName, objectName);

            return FileResponse.builder()
                    .filename(objectName)
                    .fileSize(fileSize)
                    .contentType(fileType)
                    .createdTime(createdTime)
                    .build();

        } catch (Exception e) {
            log.info("MinioServiceImpl | getFileType | Exception : {}", e.getMessage());
            return null;
        }
    }

    @Override
    public InputStream downloadObject(String bucketName, String objectName) {
        log.info("MinioServiceImpl | downloadObject is called");

        log.info("MinioServiceImpl | downloadObject | bucketName : {}", bucketName);
        log.info("MinioServiceImpl | downloadObject | objectName : {}", objectName);

        return minioUtil.getObject(bucketName,objectName);
    }

    @Override
    public boolean removeObject(String bucketName, String objectName) {
        log.info("MinioServiceImpl | removeObject is called");

        log.info("MinioServiceImpl | removeObject | bucketName : {}", bucketName);
        log.info("MinioServiceImpl | removeObject | objectName : {}", objectName);

        return minioUtil.removeObject(bucketName, objectName);
    }

    @Override
    public boolean removeListObject(String bucketName, List<String> objectNameList) {
        log.info("MinioServiceImpl | removeListObject is called");

        log.info("MinioServiceImpl | removeListObject | bucketName : {}", bucketName);
        log.info("MinioServiceImpl | removeListObject | objectNameList size : {}", objectNameList.size());

        return minioUtil.removeObject(bucketName,objectNameList);
    }

    @Override
    public String getObjectUrl(String bucketName, String objectName) {
        log.info("MinioServiceImpl | getObjectUrl is called");

        log.info("MinioServiceImpl | getObjectUrl | bucketName : {}", bucketName);
        log.info("MinioServiceImpl | getObjectUrl | objectName : {}", objectName);

        return minioUtil.getObjectUrl(bucketName, objectName);
    }
}
