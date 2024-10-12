package com.example.backend.repositorys;

import com.example.backend.common.CommonUtils;
import com.example.backend.common.DataTableResults;
import com.example.backend.common.VfData;
import com.example.backend.domain.request.RequestTypeRoomDTO;
import com.example.backend.domain.response.ResponseTypeHotelDTO;
import com.example.backend.domain.response.ResponseTypeRoomDTO;
import com.example.backend.entity.TypeHotel;
import com.example.backend.entity.TypeRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

public interface TypeRoomRepository extends JpaRepository<TypeRoom, Long> {
    boolean existsByName(String name);

    List<TypeRoom> getTypeRoomByUseYn(Boolean useYn);

    default DataTableResults<ResponseTypeRoomDTO> getDatatable(VfData vfData, RequestTypeRoomDTO request) {
        List<Object> paramList = new ArrayList<>();
        String strSql = "SELECT" + " tr.id as id," +
                " tr.name as name," +
                " tr.use_yn as useYN," +
                " tr.create_date as createDate," +
                " tr.update_date as updateDate" +
                " FROM type_room tr";
        StringBuilder strCondition = new StringBuilder("    WHERE 1 = 1");
        if (!CommonUtils.isNullOrEmpty(request.getName())){
            CommonUtils.filter(request.getName(), strCondition, paramList, "tr.name");
        }
        if (!CommonUtils.isEmpty(request.getUseYN())){
            CommonUtils.filter(request.getUseYN(), strCondition, paramList, "tr.use_yn");
        }
        String orderBy = "  ORDER BY tr.create_date DESC";
        return vfData.findPaginationQuery(strSql + strCondition.toString(), orderBy, paramList, ResponseTypeRoomDTO.class);
    }

    default DataTableResults<ResponseTypeRoomDTO> getListTypeRoomActiveByHotelId(VfData vfData, Long hotelId) {
        List<Object> paramList = new ArrayList<>();
        String strSql = "SELECT" + " th.ir as id," +
                " tr.name as name" +
                " FROM type_room tr" +
                " inner join hotel h on h.type = th.id";
        StringBuilder strCondition = new StringBuilder("    WHERE 1 = 1");
        CommonUtils.filter(Boolean.TRUE, strCondition, paramList, "tr.use_yn");
        if (!CommonUtils.isEmpty(hotelId)){
            CommonUtils.filter(hotelId, strCondition, paramList, "h.id");
        }
        String orderBy = "  ORDER BY th.create_date DESC";
        return vfData.findPaginationQuery(strSql + strCondition.toString(), orderBy, paramList, ResponseTypeRoomDTO.class);
    }
}
