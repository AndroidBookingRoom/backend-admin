package com.example.backend.domain.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
//@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseTypeHotelDTO {
     Long id;
     String name;
     Boolean useYN;
     LocalDateTime createDate;
     LocalDateTime updateDate;
}
