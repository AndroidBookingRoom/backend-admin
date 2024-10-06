package com.example.backend.repositorys;

import com.example.backend.entity.ImageDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageDetailRepository extends JpaRepository<ImageDetail, Long> {
    List<ImageDetail> findByIdImages(Long imageId);
}
