package com.example.backend.services.impl;

import com.example.backend.common.CommonUtils;
import com.example.backend.common.DataTableResults;
import com.example.backend.common.VfData;
import com.example.backend.domain.request.RequestRoomDTO;
import com.example.backend.domain.response.ResponseImageDTO;
import com.example.backend.domain.response.ResponseRoomDTO;
import com.example.backend.entity.ImageDetail;
import com.example.backend.entity.Images;
import com.example.backend.entity.Rooms;
import com.example.backend.repositorys.ImageDetailRepository;
import com.example.backend.repositorys.ImagesRepository;
import com.example.backend.repositorys.RoomRepository;
import com.example.backend.services.CommonService;
import com.example.backend.services.ImagesService;
import com.example.backend.services.RoomService;
import com.example.backend.utils.enums.ActionTypeImage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    final RoomRepository roomRepository;
    final ImagesService imagesService;

    final VfData vfData;
    final CommonService commonService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(RequestRoomDTO request) {
        log.info("[ROOM SERVICE IMPL] Start saveOrUpdate with request:id: {}", CommonUtils.convertObjectToStringJson(request.getId()));
        Rooms room;
        if (CommonUtils.isEmpty(request.getId())){
            room = createRoom(request);
        }
        else {
            room = updateRoom(request);
            if (!CommonUtils.isNullOrEmpty(request.getListImageDelete())){
                commonService.handleDeleteImageByListIdImageDetail(request.getListImageDelete(),
                        ActionTypeImage.ROOM, room.getId());
            }
        }

        if (!CommonUtils.isEmpty(request.getFileImages())) {
            commonService.uploadImage(ActionTypeImage.ROOM , room.getId(), request.getFileImages());
        }

    }

    @Override
    public DataTableResults<ResponseRoomDTO> getDataTables(RequestRoomDTO request) {
        log.info("[TYPE ROOM SERVICE IMPL] getDataTables with request:{}", CommonUtils.convertObjectToStringJson(request));
        return roomRepository.getDatatable(vfData, request);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRoomByListId(List<Long> ids) {
        checkRoomDelete();
        for (Long id : ids) {
            Rooms room = roomRepository.findById(id).orElse(null);
            if (!CommonUtils.isEmpty(room)){
                handleDeleteImageByIdRoom(room.getId());
                roomRepository.deleteById(id);
            }else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Not Found Room with id: %s", id));
            }
        }
    }

    private void handleDeleteImageByIdRoom(Long idRoom){
        Images images = imagesService.findImagesByIdRoom(idRoom).orElse(null);
        if (!CommonUtils.isEmpty(images)){
            List<ImageDetail> imageDetails = imagesService.findAllImagesDetailsByImageId(images.getId());
            List<Long> lstIdDel = imageDetails.stream().map(ImageDetail::getId).collect(Collectors.toList());
            commonService.handleDeleteImageByListIdImageDetail(lstIdDel,
                    ActionTypeImage.ROOM, idRoom);
        }
    }

    private void checkRoomDelete() {
    }

    @Override
    public ResponseRoomDTO findRoomById(Long id) {
        log.info("[ROOM SERVICE IMPL] findHotelById");
        List<ResponseRoomDTO> listData = roomRepository.findRoomById(vfData, id).getData();
        if(CommonUtils.isEmpty(listData)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Không tìm thấy phòng với id:%s", id));
        }
        ResponseRoomDTO response = listData.get(0);
        Images image = imagesService.findImagesByIdRoom(response.getId()).orElse(null);
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

    private Rooms createRoom(RequestRoomDTO request) {
        Rooms room = Rooms.builder()
                .idHotel(request.getIdHotel())
                .typeBed(request.getIdTypeBed())
                .typeRoom(request.getIdTypeRoom())
                .numberOfBedrooms(request.getNumberOfBedRooms())
                .numberOfBeds(request.getNumberOfBeds())
                .viewDirection(request.getViewDirection())
                .build();
        return roomRepository.save(room);
    }

    private Rooms updateRoom(RequestRoomDTO request) {
        Rooms room = roomRepository.findById(request.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("Không tìm thấy phòng với id:%s", request.getId())));
        room.setIdHotel(request.getIdHotel());
        room.setTypeBed(request.getIdTypeBed());
        room.setTypeRoom(request.getIdTypeRoom());
        room.setNumberOfBeds(request.getNumberOfBeds());
        room.setViewDirection(request.getViewDirection());
        room.setNumberOfBedrooms(request.getNumberOfBedRooms());
        return roomRepository.save(room);
    }

}
