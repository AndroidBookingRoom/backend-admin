package com.example.backend.services;

import com.example.backend.common.DataTableResults;
import com.example.backend.domain.request.RequestTypeBedDTO;
import com.example.backend.domain.request.RequestTypeHotelDTO;
import com.example.backend.domain.response.ResponseTypeBedDTO;
import com.example.backend.domain.response.ResponseTypeHotelDTO;

import java.util.List;

public interface TypeHotelService {
    void saveOrUpdate(RequestTypeHotelDTO request);

    DataTableResults<ResponseTypeHotelDTO> getDataTables(RequestTypeHotelDTO request);

    void deletesTypeHotel(List<Long> ids);

    List<ResponseTypeHotelDTO> getListTypeHotelActive();
}
