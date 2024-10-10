package com.example.backend.services.impl;

import com.example.backend.common.CommonUtils;
import com.example.backend.common.FileTypeUtils;
import com.example.backend.domain.FileResponse;
import com.example.backend.entity.ImageDetail;
import com.example.backend.entity.Images;
import com.example.backend.repositorys.RoomRepository;
import com.example.backend.services.CommonService;
import com.example.backend.services.ImagesService;
import com.example.backend.services.MinioService;
import com.example.backend.utils.enums.ActionTypeImage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class CommonServiceImpl implements CommonService {
    final RoomRepository roomRepository;
    final ImagesService imagesService;
    final MinioService minioService;
    @Value("${bucket-name.room}")
    String bucketNameRoom;
    @Value("${bucket-name.hotel}")
    private String bucketNameHotel;

    @Override
    public void handleDeleteImageByListIdImageDetail(List<Long> ids, ActionTypeImage actionTypeImage, Long idFk) {
        if (CommonUtils.isEmpty(idFk)){
            log.error("[handleDeleteImageByListIdImageDetail] Not start method, because idFk is empty");
            return;
        }
        Images images = imagesService.findImageByIdFK(idFk, actionTypeImage).orElse(null);
        if (CommonUtils.isEmpty(images)) {
            log.error("[handleDeleteImageByListIdImageDetail] Not found Image with action:{}, id:{}", actionTypeImage, idFk);
            return;
        }
        String bucketName = getBucketName(actionTypeImage);
        for (Long id : ids) {
            ImageDetail imageDetail = imagesService.findImageDetailById(id).orElse(null);
            if (!CommonUtils.isEmpty(imageDetail)) {
                imagesService.deleteImageDetailById(imageDetail.getId());
                minioService.removeObject(bucketName, imageDetail.getFileName());
            }
        }
    }

    @Override
    public void uploadImage(ActionTypeImage actionTypeImage, Long id,
                            MultipartFile[] multipartFiles) {
        Images images = imagesService.findImageByIdFK(id, actionTypeImage).orElse(new Images());
        String bucketName = getBucketName(actionTypeImage);
        try {
            for (MultipartFile file : multipartFiles) {
                String fileType = FileTypeUtils.getFileType(file);
                if (!CommonUtils.isEmpty(fileType)) {
                    FileResponse fileResponse = minioService.putObject(file, bucketName, fileType);
                    handleSaveImage(fileResponse, actionTypeImage, id, bucketName, images);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "File cannot be Upload");
        }
    }

    public void handleSaveImage(FileResponse fileResponse, ActionTypeImage actionTypeImage, Long id, String bucketName, Images images) {
        if (CommonUtils.isEmpty(images.getId())) {
            switch (actionTypeImage) {
                case HOTEL -> {
                    images.setIdHotel(id);
                    break;
                }
                case ROOM -> {
                    images.setIdRoom(id);
                    break;
                }
                default -> {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Chỉ xử lý ảnh với Khách sạn và Phòng");
                }
            }
            imagesService.saveImage(images);
        }
        ImageDetail imageDetail = ImageDetail.builder()
                .idImages(images.getId())
                .link(minioService.getObjectUrl(bucketName, fileResponse.getFilename()))
                .fileName(fileResponse.getFilename())
                .build();
        imagesService.saveImageDetails(imageDetail);
    }

    private String getBucketName(ActionTypeImage actionTypeImage) {
        switch (actionTypeImage) {
            case HOTEL -> {
                return bucketNameHotel;
            }
            case ROOM -> {
                return bucketNameRoom;
            }
            default -> {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Chỉ xử lý ảnh với Khách sạn và Phòng");
            }
        }
    }
}
