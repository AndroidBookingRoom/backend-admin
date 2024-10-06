package com.example.backend.services.impl;

import com.example.backend.common.CommonUtils;
import com.example.backend.common.DataTableResults;
import com.example.backend.common.FileTypeUtils;
import com.example.backend.common.VfData;
import com.example.backend.domain.FileResponse;
import com.example.backend.domain.request.RequestRoomDTO;
import com.example.backend.domain.response.ResponseRoomDTO;
import com.example.backend.entity.ImageDetail;
import com.example.backend.entity.Images;
import com.example.backend.entity.Rooms;
import com.example.backend.repositorys.RoomRepository;
import com.example.backend.services.ImagesService;
import com.example.backend.services.MinioService;
import com.example.backend.services.RoomService;
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

import java.util.List;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    final RoomRepository roomRepository;
    final ImagesService imagesService;

    final MinioService minioService;
    final VfData vfData;

    @Value("${bucket-name.room}")
    String bucketName;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(RequestRoomDTO request) {
        if (CommonUtils.isEmpty(request.getId()))
            createRoom(request);
        else
            updateRoom(request);
    }

    @Override
    public DataTableResults<ResponseRoomDTO> getDataTables(RequestRoomDTO request) {
        log.info("[TYPE ROOM SERVICE IMPL] getDataTables with request:{}", CommonUtils.convertObjectToStringJson(request));
        DataTableResults<ResponseRoomDTO> response = roomRepository.getDatatable(vfData, request);
//        if (!CommonUtils.isNullOrEmpty(response.getData())){
//            for (ResponseRoomDTO room : response.getData()) {
//                List<ImageDetail>  imageDetails = imagesService.findAllImagesDetailsByImageId(room.getImageId());
//                if (CommonUtils.isNullOrEmpty(imageDetails)){
//                }
//            }
//        }
        return response;
    }

    private void createRoom(RequestRoomDTO request) {
        Rooms room = Rooms.builder()
                .idHotel(request.getIdHotel())
                .typeBed(request.getIdTypeBed())
                .typeRoom(request.getIdTypeRoom())
                .numberOfBedrooms(request.getNumberOfBedRooms())
                .nummberOfBeds(request.getNumberOfBeds())
                .viewDirection(request.getViewDirection())
                .build();
        roomRepository.save(room);
        if (!CommonUtils.isEmpty(request.getFileImages())) {
            handleImages(request.getFileImages(), room.getId());
        }
    }

    private void updateRoom(RequestRoomDTO request) {}
    private void handleImages(MultipartFile[] fileImages, Long idRoom) {
        uploadImage(fileImages, idRoom);
    }

    private void uploadImage(MultipartFile[] multipartFiles, Long idRoom) {
        try {
            for (MultipartFile file : multipartFiles) {
                String fileType = FileTypeUtils.getFileType(file);
                if (!CommonUtils.isEmpty(fileType)){
                   FileResponse fileResponse = minioService.putObject(file, bucketName, fileType);
                   handleSaveImage(fileResponse, idRoom);
                }
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "File cannot be Upload");
        }
    }

    private void handleSaveImage(FileResponse fileResponse, Long idRoom){
        Images images = Images.builder()
                .idRoom(idRoom)
                .build();
        imagesService.saveImage(images);
        ImageDetail imageDetail = ImageDetail.builder()
                .idImages(images.getId())
                .link(minioService.getObjectUrl(bucketName, fileResponse.getFilename()))
                .build();
        imagesService.saveImageDetails(imageDetail);
    }
}
