package com.example.backend.services.impl;

import com.example.backend.common.CommonUtils;
import com.example.backend.common.DataTableResults;
import com.example.backend.common.FileTypeUtils;
import com.example.backend.common.VfData;
import com.example.backend.domain.FileResponse;
import com.example.backend.domain.request.RequestHotelDTO;
import com.example.backend.domain.response.ResponseHotelDTO;
import com.example.backend.domain.response.ResponseImageDTO;
import com.example.backend.entity.Hotel;
import com.example.backend.entity.ImageDetail;
import com.example.backend.entity.Images;
import com.example.backend.repositorys.HotelRepository;
import com.example.backend.repositorys.ImageDetailRepository;
import com.example.backend.repositorys.ImagesRepository;
import com.example.backend.services.CommonService;
import com.example.backend.services.HotelService;
import com.example.backend.services.ImagesService;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HotelServiceImpl implements HotelService {
    final HotelRepository hotelRepository;
    final CommonService commonService;
    final ImagesService imagesService;
    final VfData vfData;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(RequestHotelDTO requestDTO) {
        log.info("[HOTEL SERVICE IMPL] Start saveOrUpdate with request:id: {}", CommonUtils.convertObjectToStringJson(requestDTO.getId()));
        Hotel hotel;
        if (CommonUtils.isEmpty(requestDTO.getId())) {
            //handle create
            hotel = handleCreateHotel(requestDTO);
        } else {
//            handle update
            hotel = handleUpdateHotel(requestDTO);
            if (!CommonUtils.isNullOrEmpty(requestDTO.getListImageDelete())){
                commonService.handleDeleteImageByListIdImageDetail(requestDTO.getListImageDelete(),
                        ActionTypeImage.HOTEL, hotel.getId());
            }
        }

        if (!CommonUtils.isEmpty(requestDTO.getFileImages())) {
            commonService.uploadImage(ActionTypeImage.HOTEL , hotel.getId(), requestDTO.getFileImages());
        }

    }

    @Override
    public DataTableResults<ResponseHotelDTO> getDataTables(RequestHotelDTO request) {
        log.info("[HOTEL SERVICE IMPL] getDataTables with request:{}", CommonUtils.convertObjectToStringJson(request));
        return hotelRepository.getDatatable(vfData, request);
    }

    @Override
    public ResponseHotelDTO findHotelById(Long id) {
        log.info("[HOTEL SERVICE IMPL] findHotelById");
        List<ResponseHotelDTO> listData = hotelRepository.findHotelById(vfData, id).getData();
        if(CommonUtils.isEmpty(listData)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Không tìm thấy khách sạn với id:%s", id));
        }
        ResponseHotelDTO response = listData.get(0);
        Images image = imagesService.findImagesByIdHotel(response.getId()).orElse(null);
        if (!CommonUtils.isEmpty(image)) {
            List<ImageDetail> imageDetails = imagesService.findAllImagesDetailsByImageId(image.getId());
            List<ResponseImageDTO> images = new ArrayList<>();
            for (ImageDetail item : imageDetails) {
                ResponseImageDTO it = ResponseImageDTO.builder()
                        .idImage(image.getId())
                        .idImageDetail(item.getId())
                        .url(item.getLink())
                        .build();
                images.add(it);
            }
            response.setImages(images);

        }
        return response;
    }

    @Override
    public List<ResponseHotelDTO> getAllListHotels() {
        return hotelRepository.findAllHotel(vfData);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteHotelByListId(List<Long> ids) {
        checkHotelDelete();
        for (Long id : ids) {
            Hotel hotel = hotelRepository.findById(id).orElse(null);
            if (!CommonUtils.isEmpty(hotel)) {
                handleDeleteImageByIdHotel(hotel.getId());
                hotelRepository.deleteById(id);
            }else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Not Found Hotel with id: %s", id));
            }
        }
    }

    private void handleDeleteImageByIdHotel(Long idHotel){
        Images images = imagesService.findImagesByIdHotel(idHotel).orElse(null);
        if (!CommonUtils.isEmpty(images)){
            List<ImageDetail> imageDetails = imagesService.findAllImagesDetailsByImageId(images.getId());
            List<Long> lstIdDel = imageDetails.stream().map(ImageDetail::getId).collect(Collectors.toList());
            commonService.handleDeleteImageByListIdImageDetail(lstIdDel,
                    ActionTypeImage.HOTEL, idHotel);
        }
    }


    private void checkHotelDelete() {}

    private Hotel handleCreateHotel(RequestHotelDTO request) {
        Hotel hotel = Hotel.builder()
                .nameHotel(request.getNameHotel())
                .type(request.getType())
                .address(request.getAddress())
                .location(request.getLocation())
                .service(request.getService())
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
        hotel.setService(request.getService());
        if (!CommonUtils.isNullOrEmpty(request.getListImageDelete()) || !CommonUtils.isEmpty(request.getFileImages()))
            hotel.setUpdateDate(LocalDateTime.now());
        return hotelRepository.save(hotel);
    }
}
