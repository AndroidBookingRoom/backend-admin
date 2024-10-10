package com.example.backend.services;

import com.example.backend.common.DataTableResults;
import com.example.backend.domain.request.RequestHotelDTO;
import com.example.backend.domain.request.RequestTypeBedDTO;
import com.example.backend.domain.response.ResponseHotelDTO;

public interface HotelService {
    void saveOrUpdate(RequestHotelDTO requestDTO);

    DataTableResults<ResponseHotelDTO> getDataTables(RequestHotelDTO request);

    ResponseHotelDTO findHotelById(Long id);
}
