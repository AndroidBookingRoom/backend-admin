package com.example.backend.domain.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Tolerate;

import java.time.LocalDateTime;

@Getter
@Setter
//@NoArgsConstructor
@Builder
public class ResponseTypeRoomDTO {
    @Tolerate
    public ResponseTypeRoomDTO() {
    }

    private Long id;
    private String name;
    private Boolean useYN;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}
