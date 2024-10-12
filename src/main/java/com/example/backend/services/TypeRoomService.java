package com.example.backend.services;

import com.example.backend.common.DataTableResults;
import com.example.backend.domain.request.RequestTypeRoomDTO;
import com.example.backend.domain.response.ResponseTypeHotelDTO;
import com.example.backend.domain.response.ResponseTypeRoomDTO;

import java.util.List;

public interface TypeRoomService {
    void saveOrUpdate(RequestTypeRoomDTO request);

    DataTableResults<ResponseTypeRoomDTO> getDataTables(RequestTypeRoomDTO request);

    void deletesTypeRoom(List<Long> ids);

    List<ResponseTypeRoomDTO> getListTypeRoomActive();
    List<ResponseTypeRoomDTO> getListTypeHotelActiveByHotelId(Long hotelId);
}
