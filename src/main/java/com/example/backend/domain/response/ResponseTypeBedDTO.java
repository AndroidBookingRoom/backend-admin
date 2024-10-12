package com.example.backend.domain.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ResponseTypeBedDTO {
    @Tolerate
    public ResponseTypeBedDTO() {
    }

    private Long id;
    private String name;
    private Boolean useYN;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}
