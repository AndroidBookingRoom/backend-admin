package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@SuperBuilder
@Getter
@Setter
@Entity(name = "combo")
@NoArgsConstructor
public class Combo extends BaseAudits{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name ="airlines")
    private String airlines;

    @Column(name = "round_trip_shuttle")
    @Builder.Default
    private Boolean roundTripShuttle = Boolean.FALSE;

    @Column(name = "location")
    private String location;

    @Column(name = "terms_and_conditions")
    private String termsAndConditions;

    @Column(name = "time")
    private String time;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "event_type")
    private Long eventType;
}
