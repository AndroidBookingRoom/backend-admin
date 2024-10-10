package com.example.backend.repositorys;

import com.example.backend.entity.Images;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImagesRepository extends JpaRepository<Images, Long> {
    Optional<Images> findImagesByIdHotel(Long idHotel);
    Optional<Images> findImagesByIdRoom(Long idRoom);
}
