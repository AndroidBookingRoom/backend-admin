package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@SuperBuilder
@Entity(name = "hotel")
@NoArgsConstructor
public class Hotel extends BaseAudits {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "type")
    private Long type;

    @Column(name = "name_hotel")
    private String nameHotel;

    @Column(name = "address")
    private String address;

    @Column(name = "location")
    private String location;

    @Column(name = "service")
    private String service;

}
