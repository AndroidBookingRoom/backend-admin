package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@Entity(name = "event_type")
@NoArgsConstructor
public class EventType extends BaseAudits{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "event_name")
    private String eventName;

    @Column(name = "use_YN")
    @Builder.Default
    private Boolean useYN  = Boolean.TRUE;
}
