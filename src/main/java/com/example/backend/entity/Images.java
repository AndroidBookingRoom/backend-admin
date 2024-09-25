package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@Entity(name = "tbl_images")
@NoArgsConstructor
public class Images extends BaseAudits{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "id_room")
    private Long idRoom;

    @Column(name = "id_hotel")
    private Long idHotel;

    @Column(name = "id_tour")
    private Long idTour;

    @Column(name = "id_combo")
    private Long idCombo;
}
