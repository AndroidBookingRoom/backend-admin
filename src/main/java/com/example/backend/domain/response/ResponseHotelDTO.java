package com.example.backend.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseHotelDTO {
    Long id;
    Long type;
    String nameHotel;
    String nameTypeHotel;
    String address;
    String location;
    String service;
    LocalDateTime createDate;
    LocalDateTime updateDate;
    List<ResponseImageDTO> images;
}
