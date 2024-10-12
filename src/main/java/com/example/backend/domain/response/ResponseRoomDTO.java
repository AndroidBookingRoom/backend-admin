package com.example.backend.domain.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseRoomDTO {
    Long id;
    Long idTypeRoom;
    Long idTypeBed;
    Long idHotel;
    String typeBedName;
    String typeRoomName;
    String nameHotel;
    Integer numberOfBedRooms;
    Integer numberOfBeds;
    String viewDirection;
    LocalDateTime createDate;
    LocalDateTime updateDate;
    List<ResponseImageDTO> images;
}
