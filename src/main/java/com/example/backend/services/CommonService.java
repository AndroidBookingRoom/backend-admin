package com.example.backend.services;

import com.example.backend.utils.enums.ActionTypeImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CommonService {
    void uploadImage(ActionTypeImage actionTypeImage, Long id, MultipartFile[] multipartFiles);

    void handleDeleteImageByListIdImageDetail(List<Long> ids, ActionTypeImage actionTypeImage, Long idFK);
}
