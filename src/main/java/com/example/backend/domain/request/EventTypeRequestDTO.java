package com.example.backend.domain.request;

import com.example.backend.utils.enums.EventTypes;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventTypeRequestDTO {
    private Long id;
    private Long idTour;
    private Long idCombo;
    @NotBlank
    private String eventName;
    private Boolean useYN;
    private EventTypes eventTypes;
}
