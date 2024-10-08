package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


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
}
