package com.example.backend.domain.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class RequestHotelDTO {
    private MultipartFile[] fileImages;
}
