package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@Entity(name = "combo_detail")
@NoArgsConstructor
public class ComboDetail extends BaseAudits{

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "id_combo")
    private Long idCombo;

    @Column(name = "description")
    private String description;

}
