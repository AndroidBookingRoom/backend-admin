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
                "   td.name as typeBedName," +
                "   tr.name as typeRoomName," +
                "   ht.name_hotel as nameHotel," +
                "   r.number_of_bedrooms as numberOfBedRooms," +
                "   r.number_of_beds as numberOfBeds," +
                "   r.view_direction as viewDirection," +
                "   r.create_date as createDate," +
                "   r.updateDate as updateDate" +
                " FROM rooms r" +
                " inner join type_bed tb on tb.id = r.type_bed" +
                " inner join type_room tr on tr.id = r.type_room" +
                " inner join hotel ht on ht.id = r.id_hotel" +
                " inner join images img on img.id_room = r.id";
//                " inner join image_detail imgd on imgd.id_images = img.id";
        StringBuilder strCondition = new StringBuilder("    WHERE 1 = 1");
//        if (!CommonUtils.isNullOrEmpty(request.getName())){
//            CommonUtils.filter(request.getName(), strCondition, paramList, "tr.name");
//        }
//        if (!CommonUtils.isEmpty(request.getUseYN())){
//            CommonUtils.filter(request.getUseYN(), strCondition, paramList, "tr.use_yn");
//        }
        String orderBy = "  ORDER BY tr.create_date DESC";
        return vfData.findPaginationQuery(strSql + strCondition.toString(), orderBy, paramList, ResponseRoomDTO.class);
    }
}
