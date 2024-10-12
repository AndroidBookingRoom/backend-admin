package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
@Setter
@Entity(name = "tour_detail")
@SuperBuilder
@NoArgsConstructor
public class TourDetail extends BaseAudits{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "id_tour")
    private Long idTour;

    @Column(name = "description")
    private String description;

    @Column(name = "departure_date")
    private LocalDateTime departureDate;

    @Column(name = "procedures_visa")
    private String proceduresVisa; // Thủ tục, visa

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "departure_address")
    private String departureAddress;

    @Column(name = "location_tour")
    private String locationTour;

    @Column(name = "airlines")
    private String airlines;
}
