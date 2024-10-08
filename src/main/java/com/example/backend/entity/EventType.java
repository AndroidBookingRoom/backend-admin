package com.example.backend.entity;

import com.example.backend.utils.converters.EventTypesConverter;
import com.example.backend.utils.enums.EventTypes;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;


@SuperBuilder
@Getter
@Setter
@Entity(name = "event_type")
@NoArgsConstructor
@AllArgsConstructor
public class EventType extends BaseAudits{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "id_combo")
    private Long idCombo;

    @Column(name = "id_tour")
    private Long idTour;

    @Column(name = "event_name")
    private String eventName;

    @Column(name = "use_YN")
    @Builder.Default
    private Boolean useYN  = Boolean.TRUE;

    @Column(name = "type")
    @Convert(converter = EventTypesConverter.class)
    private EventTypes eventTypes;
}
