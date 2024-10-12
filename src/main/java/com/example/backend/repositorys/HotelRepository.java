package com.example.backend.repositorys;

import com.example.backend.common.CommonUtils;
import com.example.backend.common.DataTableResults;
import com.example.backend.common.VfData;
import com.example.backend.domain.request.RequestHotelDTO;
import com.example.backend.domain.response.ResponseHotelDTO;
import com.example.backend.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long> {

    default DataTableResults<ResponseHotelDTO> getDatatable(VfData vfData, RequestHotelDTO request) {
        List<Object> paramList = new ArrayList<>();
        String strSql = "SELECT" + " ht.id as id," +
                " ht.name_hotel as nameHotel," +
                " ht.service as service," +
                " ht.address as address," +
                " ht.location as location," +
                " thl.name as nameTypeHotel," +
                " thl.id as type," +
                " ht.create_date as createDate," +
                " ht.update_date as updateDate" +
                " FROM hotel ht" +
                " INNER JOIN type_hotel thl on thl.id = ht.type AND thl.use_yn = 1 ";
        StringBuilder strCondition = new StringBuilder("    WHERE 1 = 1");
        if (!CommonUtils.isNullOrEmpty(request.getNameHotel())) {
            CommonUtils.filter(request.getNameHotel(), strCondition, paramList, "ht.name_hotel");
        }
        if (!CommonUtils.isEmpty(request.getAddress())) {
            CommonUtils.filter(request.getAddress(), strCondition, paramList, "ht.address");
        }
        if (!CommonUtils.isEmpty(request.getLocation())) {
            CommonUtils.filter(request.getLocation(), strCondition, paramList, "ht.location");
        }
        if (!CommonUtils.isEmpty(request.getType())) {
            CommonUtils.filter(request.getType(), strCondition, paramList, "thl.id");
        }
        String orderBy = "  ORDER BY ht.create_date DESC";
        return vfData.findPaginationQuery(strSql + strCondition.toString(), orderBy, paramList, ResponseHotelDTO.class);
    }

    default DataTableResults<ResponseHotelDTO> findHotelById(VfData vfData, Long id) {
        List<Object> paramList = new ArrayList<>();
        String strSql = "SELECT" + " ht.id as id," +
                " ht.name_hotel as nameHotel," +
                " ht.service as service," +
                " ht.address as address," +
                " ht.location as location," +
                " thl.name as nameTypeHotel," +
                " thl.id as type," +
                " ht.create_date as createDate," +
                " ht.update_date as updateDate" +
                " FROM hotel ht" +
                " INNER JOIN type_hotel thl on thl.id = ht.type AND thl.use_yn = 1 ";
        StringBuilder strCondition = new StringBuilder("    WHERE 1 = 1");
        if (!CommonUtils.isEmpty(id)) {
            CommonUtils.filter(id, strCondition, paramList, "ht.id");
        }
        String orderBy = "  ORDER BY ht.create_date DESC";
        return vfData.findPaginationQuery(strSql + strCondition.toString(), orderBy, paramList, ResponseHotelDTO.class);
    }

    default List<ResponseHotelDTO> findAllHotel(VfData vfData) {
        List<Object> paramList = new ArrayList<>();
        String strSql = "SELECT" + " ht.id as id," +
                " ht.name_hotel as nameHotel" +
                " FROM hotel ht" +
                " INNER JOIN type_hotel thl on thl.id = ht.type AND thl.use_yn = 1 ";
        String orderBy = "  ORDER BY ht.create_date DESC";
        return vfData.findAllData(strSql, orderBy, paramList, ResponseHotelDTO.class);
    }
}
