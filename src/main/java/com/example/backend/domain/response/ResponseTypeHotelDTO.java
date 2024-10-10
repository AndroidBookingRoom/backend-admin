package com.example.backend.domain.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.Tolerate;

import java.time.LocalDateTime;

@Getter
@Setter
//@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseTypeHotelDTO {
     @Tolerate
     public ResponseTypeHotelDTO() {
     }

     Long id;
     String name;
     Boolean useYN;
     LocalDateTime createDate;
     LocalDateTime updateDate;
}
