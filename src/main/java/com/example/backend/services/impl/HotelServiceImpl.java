package com.example.backend.services.impl;

import com.example.backend.common.CommonUtils;
import com.example.backend.common.FileTypeUtils;
import com.example.backend.domain.request.RequestHotelDTO;
import com.example.backend.services.HotelService;
import com.example.backend.services.MinioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {
    private final MinioService minioService;

    @Value("${bucket-name.hotel}")
    private String bucketName;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(RequestHotelDTO requestDTO) {
        if (!CommonUtils.isEmpty(requestDTO.getFileImages())) {
            uploadImage(requestDTO.getFileImages());
        }
    }

    private void uploadImage(MultipartFile[] multipartFiles) {
        try {
            for (MultipartFile file : multipartFiles) {
                String fileType = FileTypeUtils.getFileType(file);
                if (!CommonUtils.isEmpty(fileType)){
                    minioService.putObject(file, bucketName, fileType);
                }
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "File cannot be Upload");
        }
    }
}
