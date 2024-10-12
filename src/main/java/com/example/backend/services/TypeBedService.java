package com.example.backend.services;

import com.example.backend.common.DataTableResults;
import com.example.backend.domain.request.RequestTypeBedDTO;
import com.example.backend.domain.request.RequestTypeRoomDTO;
import com.example.backend.domain.response.ResponseTypeBedDTO;
import com.example.backend.domain.response.ResponseTypeRoomDTO;

import java.util.List;

public interface TypeBedService {
    void saveOrUpdate(RequestTypeBedDTO request);

    DataTableResults<ResponseTypeBedDTO> getDataTables(RequestTypeBedDTO request);

    void deletesTypeRoom(List<Long> ids);

    List<ResponseTypeBedDTO> getListTypeBedActive();

}
