package com.example.backend.services.impl;

import com.example.backend.entity.ImageDetail;
import com.example.backend.entity.Images;
import com.example.backend.repositorys.ImageDetailRepository;
import com.example.backend.repositorys.ImagesRepository;
import com.example.backend.services.ImagesService;
import com.example.backend.utils.enums.ActionTypeImage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ImagesServiceImpl implements ImagesService {
    final ImagesRepository imagesRepository;
    final ImageDetailRepository imageDetailRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveImage(Images image) {
        imagesRepository.save(image);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveImageDetails(ImageDetail imageDetail) {
        imageDetailRepository.save(imageDetail);
    }

    @Override
    public Optional<Images> findImageByIdFK(Long imageId, ActionTypeImage actionTypeImage) {
        switch (actionTypeImage) {
            case HOTEL -> {
                return imagesRepository.findImagesByIdHotel(imageId);
            }
            case ROOM -> {
                return imagesRepository.findImagesByIdRoom(imageId);
            }
            default -> {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Chỉ xử lý ảnh với Khách sạn và Phòng");
            }
        }
    }

    @Override
    public List<ImageDetail> findAllImagesDetailsByImageId(Long imageId) {
        return imageDetailRepository.findByIdImages(imageId);
    }

    @Override
    public void deleteImageDetailById(Long imageId) {
        imageDetailRepository.deleteById(imageId);
    }

    @Override
    public Optional<ImageDetail> findImageDetailById(Long imageId) {
        return imageDetailRepository.findById(imageId);
    }
}
