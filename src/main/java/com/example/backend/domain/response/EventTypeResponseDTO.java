package com.example.backend.domain.response;

import com.example.backend.utils.enums.EventTypes;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventTypeResponseDTO {
    private Long id;
    private Long idTour;
    private Long idCombo;
    private String eventName;
    private Boolean useYN;
    private EventTypes eventTypes;
}
