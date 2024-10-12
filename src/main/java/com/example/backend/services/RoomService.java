package com.example.backend.services;

import com.example.backend.common.DataTableResults;
import com.example.backend.domain.request.RequestRoomDTO;
import com.example.backend.domain.response.ResponseRoomDTO;

import java.util.List;

public interface RoomService {
    void saveOrUpdate(RequestRoomDTO request);

    DataTableResults<ResponseRoomDTO> getDataTables(RequestRoomDTO request);

    ResponseRoomDTO findRoomById(Long id);

    void deleteRoomByListId(List<Long> ids);
}
