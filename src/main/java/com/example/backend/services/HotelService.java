package com.example.backend.services;

import com.example.backend.domain.request.RequestHotelDTO;

public interface HotelService {
    void saveOrUpdate(RequestHotelDTO requestDTO);
}
