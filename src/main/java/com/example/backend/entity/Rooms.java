package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@Entity(name = "rooms")
@NoArgsConstructor
public class Rooms extends BaseAudits{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "type_room")
    private Long typeRoom;

    @Column(name = "type_bed")
    private Long typeBed;

    @Column(name = "id_hotel")
    private Long idHotel;

    @Column(name = "number_of_bedrooms")
    private Integer numberOfBedrooms;

    @Column(name = "number_of_beds")
    private Integer nummberOfBeds;

    @Column(name = "view_direction")
    private String viewDirection;

}
