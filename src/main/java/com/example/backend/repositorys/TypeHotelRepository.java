package com.example.backend.repositorys;

import com.example.backend.common.CommonUtils;
import com.example.backend.common.DataTableResults;
import com.example.backend.common.VfData;
import com.example.backend.domain.request.RequestTypeBedDTO;
import com.example.backend.domain.request.RequestTypeHotelDTO;
import com.example.backend.domain.response.ResponseTypeBedDTO;
import com.example.backend.domain.response.ResponseTypeHotelDTO;
import com.example.backend.entity.TypeHotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

public interface TypeHotelRepository extends JpaRepository<TypeHotel, Long> {
    boolean existsByName(String name);

    List<TypeHotel> getTypeHotelByUseYn(Boolean useYn);

    boolean existsByNameAndIdNotIn(String name, List<Long> id);

    default DataTableResults<ResponseTypeHotelDTO> getDatatable(VfData vfData, RequestTypeHotelDTO request) {
        List<Object> paramList = new ArrayList<>();
        String strSql = "SELECT" + " th.id as id," +
                " th.name as name," +
                " th.use_yn as useYN," +
                " th.create_date as createDate," +
                " th.update_date as updateDate" +
                " FROM type_hotel th";
        StringBuilder strCondition = new StringBuilder("    WHERE 1 = 1");
        if (!CommonUtils.isNullOrEmpty(request.getName())){
            CommonUtils.filter(request.getName(), strCondition, paramList, "th.name");
        }
        if (!CommonUtils.isEmpty(request.getUseYN())){
            CommonUtils.filter(request.getUseYN(), strCondition, paramList, "th.use_yn");
        }
        String orderBy = "  ORDER BY th.create_date DESC";
        return vfData.findPaginationQuery(strSql + strCondition.toString(), orderBy, paramList, ResponseTypeHotelDTO.class);
    }

    default DataTableResults<ResponseTypeHotelDTO> getListTypeHotelActiveByHotelId(VfData vfData, Long hotelId) {
        List<Object> paramList = new ArrayList<>();
        String strSql = "SELECT" + " th.id as id," +
                " th.name as name," +
                " FROM type_hotel th" +
                " inner join hotel h on h.type = th.id";
        StringBuilder strCondition = new StringBuilder("    WHERE 1 = 1");
        CommonUtils.filter(Boolean.TRUE, strCondition, paramList, "th.use_yn");
        if (!CommonUtils.isEmpty(hotelId)){
            CommonUtils.filter(hotelId, strCondition, paramList, "h.id");
        }
        String orderBy = "  ORDER BY th.create_date DESC";
        return vfData.findPaginationQuery(strSql + strCondition.toString(), orderBy, paramList, ResponseTypeHotelDTO.class);
    }
}
