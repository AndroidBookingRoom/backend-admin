package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@SuperBuilder
@Entity(name = "image_detail")
@NoArgsConstructor
public class ImageDetail extends BaseAudits{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "id_images")
    private Long idImages;

    @Column(name = "link", columnDefinition = "LONGTEXT")
    private String link;

    @Column(name = "file_name")
    private String fileName; // use save for delete in minio
}
