package com.example.backend.domain.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseTypeHotelDTO {
     Long id;
     String name;
     Boolean useYN;
     LocalDateTime createDate;
     LocalDateTime updateDate;
}
