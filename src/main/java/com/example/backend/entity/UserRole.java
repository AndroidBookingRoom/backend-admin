package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@Entity(name = "user_role")
@NoArgsConstructor
public class UserRole extends BaseAudits{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "id_role")
    private Long idRole;

    @Column(name = "id_user")
    private Long idUser;
}
