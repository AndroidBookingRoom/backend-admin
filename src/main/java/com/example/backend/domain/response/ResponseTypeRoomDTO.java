package com.example.backend.domain.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ResponseTypeRoomDTO {
    private Long id;
    private String name;
    private Boolean useYN;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}
