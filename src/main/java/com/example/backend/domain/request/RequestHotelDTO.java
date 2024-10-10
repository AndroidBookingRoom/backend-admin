package com.example.backend.domain.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestHotelDTO {
    Long id;
    String nameHotel;
    String location;
    String address;
    Long type; //Type hotel
    String service;
    MultipartFile[] fileImages;
    List<Long> listImageDelete;
}
