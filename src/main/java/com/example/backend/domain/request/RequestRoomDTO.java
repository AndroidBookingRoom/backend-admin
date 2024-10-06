package com.example.backend.domain.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class RequestRoomDTO {
    Long id;
//    @NonNull
    Long idTypeRoom;
//    @NonNull
    Long idTypeBed;
//    @NonNull
    Long idHotel;
    String viewDirection;
    Integer numberOfBedRooms;
    Integer numberOfBeds;
    MultipartFile[] fileImages;
}
