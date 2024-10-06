package com.example.backend.services;

import com.example.backend.entity.ImageDetail;
import com.example.backend.entity.Images;

import java.util.List;


public interface ImagesService {
    void saveImage(Images image);
    void saveImageDetails(ImageDetail imageDetail);
    List<ImageDetail> findAllImagesDetailsByImageId(Long imageId);
}
