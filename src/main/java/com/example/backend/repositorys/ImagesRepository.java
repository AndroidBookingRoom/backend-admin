package com.example.backend.repositorys;

import com.example.backend.entity.Images;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImagesRepository extends JpaRepository<Images, Integer> {
}
