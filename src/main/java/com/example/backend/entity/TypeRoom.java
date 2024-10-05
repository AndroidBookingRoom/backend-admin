package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@Entity(name = "type_room")
@SuperBuilder
@NoArgsConstructor
public class TypeRoom extends BaseAudits {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "use_YN")
    @Builder.Default
    private Boolean useYn = Boolean.TRUE;
}
