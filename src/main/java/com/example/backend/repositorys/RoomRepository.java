package com.example.backend.repositorys;

import com.example.backend.common.CommonUtils;
import com.example.backend.common.DataTableResults;
import com.example.backend.common.VfData;
import com.example.backend.domain.request.RequestRoomDTO;
import com.example.backend.domain.request.RequestTypeRoomDTO;
import com.example.backend.domain.response.ResponseRoomDTO;
import com.example.backend.domain.response.ResponseTypeRoomDTO;
import com.example.backend.entity.Rooms;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

public interface RoomRepository  extends JpaRepository<Rooms, Long> {

    default DataTableResults<ResponseRoomDTO> getDatatable(VfData vfData, RequestRoomDTO request) {
        List<Object> paramList = new ArrayList<>();
        String strSql = "SELECT" + " r.id as id," +
                "   tb.name as typeBedName," +
                "   tr.name as typeRoomName," +
                "   ht.name_hotel as nameHotel," +
                "   r.number_of_bedrooms as numberOfBedRooms," +
                "   r.number_of_beds as numberOfBeds," +
                "   r.view_direction as viewDirection," +
                "   r.create_date as createDate," +
                "   r.update_date as updateDate" +
                " FROM rooms r" +
                " inner join type_bed tb on tb.id = r.type_bed AND tb.use_yn = 1" +
                " inner join type_room tr on tr.id = r.type_room AND tr.use_yn = 1 " +
                " inner join hotel ht on ht.id = r.id_hotel";
        StringBuilder strCondition = new StringBuilder("    WHERE 1 = 1");
        if (!CommonUtils.isEmpty(request.getIdTypeRoom())){
            CommonUtils.filter(request.getIdTypeRoom(), strCondition, paramList, "tr.id");
        }
        if (!CommonUtils.isEmpty(request.getIdHotel())){
            CommonUtils.filter(request.getIdHotel(), strCondition, paramList, "ht.id");
        }

        if (!CommonUtils.isEmpty(request.getIdTypeBed())){
            CommonUtils.filter(request.getIdTypeBed(), strCondition, paramList, "tb.id");
        }
        String orderBy = "  ORDER BY tr.create_date DESC";
        return vfData.findPaginationQuery(strSql + strCondition.toString(), orderBy, paramList, ResponseRoomDTO.class);
    }

    default DataTableResults<ResponseRoomDTO> findRoomById(VfData vfData, Long id) {
        List<Object> paramList = new ArrayList<>();
        String strSql = "SELECT" + " r.id as id," +
                "   tb.id as idTypeBed," +
                "   tr.id as idTypeRoom," +
                "   ht.id as idHotel," +
                "   r.number_of_bedrooms as numberOfBedRooms," +
                "   r.number_of_beds as numberOfBeds," +
                "   r.view_direction as viewDirection" +
                " FROM rooms r" +
                " inner join type_bed tb on tb.id = r.type_bed AND tb.use_yn = 1" +
                " inner join type_room tr on tr.id = r.type_room AND tr.use_yn = 1" +
                " inner join hotel ht on ht.id = r.id_hotel";
        StringBuilder strCondition = new StringBuilder("    WHERE 1 = 1");
        if (!CommonUtils.isEmpty(id)){
            CommonUtils.filter(id, strCondition, paramList, "r.id");
        }
        String orderBy = "  ORDER BY tr.create_date DESC";
        return vfData.findPaginationQuery(strSql + strCondition.toString(), orderBy, paramList, ResponseRoomDTO.class);
    }
}
