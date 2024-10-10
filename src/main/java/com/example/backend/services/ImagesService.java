package com.example.backend.services;

import com.example.backend.entity.ImageDetail;
import com.example.backend.entity.Images;
import com.example.backend.utils.enums.ActionTypeImage;

import java.util.List;
import java.util.Optional;


public interface ImagesService {
    void saveImage(Images image);
    void saveImageDetails(ImageDetail imageDetail);
    List<ImageDetail> findAllImagesDetailsByImageId(Long imageId);
    Optional<Images> findImageByIdFK(Long imageId, ActionTypeImage actionTypeImage);
    void deleteImageDetailById(Long imageId);
    Optional<ImageDetail> findImageDetailById(Long imageId);
}
