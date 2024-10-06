package com.example.backend.services.impl;

import com.example.backend.entity.ImageDetail;
import com.example.backend.entity.Images;
import com.example.backend.repositorys.ImageDetailRepository;
import com.example.backend.repositorys.ImagesRepository;
import com.example.backend.services.ImagesService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public List<ImageDetail> findAllImagesDetailsByImageId(Long imageId) {
        return imageDetailRepository.findByIdImages(imageId);
    }
}
