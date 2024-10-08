package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@SuperBuilder
@Entity(name = "role")
@NoArgsConstructor
public class Role extends BaseAudits{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "role_name")
    private String roleName;

    @Column(name = "use_YN")
    @Builder.Default
    private Boolean useYN= Boolean.TRUE;
}
