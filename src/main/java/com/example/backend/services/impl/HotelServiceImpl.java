package com.example.backend.services.impl;

import com.example.backend.common.CommonUtils;
import com.example.backend.common.FileTypeUtils;
import com.example.backend.domain.FileResponse;
import com.example.backend.domain.request.RequestHotelDTO;
import com.example.backend.entity.Hotel;
import com.example.backend.repositorys.HotelRepository;
import com.example.backend.services.CommonService;
import com.example.backend.services.HotelService;
import com.example.backend.services.MinioService;
import com.example.backend.utils.enums.ActionTypeImage;
import com.google.gson.Gson;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HotelServiceImpl implements HotelService {
    final MinioService minioService;
    final HotelRepository hotelRepository;
    final CommonService commonService;

    @Value("${bucket-name.hotel}")
    private String bucketName;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(RequestHotelDTO requestDTO) {
//        log.info("[HOTEL SERVICE IMPL] Start saveOrUpdate with request: {}", new Gson().toJson(requestDTO));
        Hotel hotel;
        if (CommonUtils.isEmpty(requestDTO.getId())) {
            //handle create
            hotel = handleCreateHotel(requestDTO);
        } else {
//            handle update
            hotel = handleUpdateHotel(requestDTO);
        }

        if (!CommonUtils.isEmpty(requestDTO.getFileImages())) {
            commonService.uploadImage(ActionTypeImage.HOTEL , hotel.getId(), requestDTO.getFileImages());
        }
    }

    private Hotel handleCreateHotel(RequestHotelDTO request) {
        Hotel hotel = Hotel.builder()
                .nameHotel(request.getNameHotel())
                .type(request.getType())
                .address(request.getAddress())
                .location(request.getLocation())
                .build();
        return hotelRepository.save(hotel);
    }

    private Hotel handleUpdateHotel(RequestHotelDTO request) {
        Hotel hotel = hotelRepository.findById(request.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("Không tìm thấy khách sạn với id:%s", request.getId())));
        hotel.setNameHotel(request.getNameHotel());
        hotel.setType(request.getType());
        hotel.setAddress(request.getAddress());
        hotel.setLocation(request.getLocation());
        return hotelRepository.save(hotel);
    }
}
