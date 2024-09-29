package com.example.backend.repositorys;

import com.example.backend.common.CommonUtils;
import com.example.backend.common.config.DataTableResults;
import com.example.backend.common.config.VfData;
import com.example.backend.domain.response.EventTypeResponseDTO;
import com.example.backend.entity.EventType;
import com.example.backend.utils.enums.EventTypes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

public interface EventTypeRepository extends JpaRepository<EventType, Long> {

    List<EventType> findByEventTypes(EventTypes eventTypes);
    Boolean  existsAllByEventName(String eventName);
    public default DataTableResults<EventTypeResponseDTO> getDatatable(VfData vfData, EventTypes eventTypes) {
        List<Object> paramList = new ArrayList<>();
        String sql = "SELECT ";
        sql += "    et.id as id, ";
        sql += "    et.id_combo as idCombo, ";
//        sql += "    et.id_tour as idTour, ";
        sql += "    et.event_name as eventName ";
        sql += "    et.use_YN as useYN";
//        sql += "    et.use_YN as useYN";
        sql += "    FROM event_type et ";

        StringBuilder strCondition = new StringBuilder(" WHERE 1 = 1 ");
        if (!CommonUtils.isEmpty(eventTypes)) {
            CommonUtils.filter(eventTypes.getValue(), strCondition, paramList, "et.type");
        }
        String orderBy = " ORDER BY et.create_date DESC ";
        return vfData.findPaginationQuery(sql + strCondition.toString(), orderBy, paramList, EventTypeResponseDTO.class);
    }
}
