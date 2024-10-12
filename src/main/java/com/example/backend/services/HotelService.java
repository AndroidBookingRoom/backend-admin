package com.example.backend.services;

import com.example.backend.common.DataTableResults;
import com.example.backend.domain.request.RequestHotelDTO;
import com.example.backend.domain.request.RequestTypeBedDTO;
import com.example.backend.domain.response.ResponseHotelDTO;

import java.util.List;

public interface HotelService {
    void saveOrUpdate(RequestHotelDTO requestDTO);

    DataTableResults<ResponseHotelDTO> getDataTables(RequestHotelDTO request);

    ResponseHotelDTO findHotelById(Long id);

    void deleteHotelByListId(List<Long> ids);

    List<ResponseHotelDTO> getAllListHotels();
}
