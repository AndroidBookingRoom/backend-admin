package com.example.backend.services;

import com.example.backend.utils.enums.ActionTypeImage;
import org.springframework.web.multipart.MultipartFile;

public interface CommonService {
    void uploadImage(ActionTypeImage actionTypeImage, Long id, MultipartFile[] multipartFiles);
}
