package com.example.backend.repositorys;

import com.example.backend.common.CommonUtils;
import com.example.backend.common.DataTableResults;
import com.example.backend.common.VfData;
import com.example.backend.domain.request.RequestTypeBedDTO;
import com.example.backend.domain.response.ResponseTypeBedDTO;
import com.example.backend.entity.TypeBed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

public interface TypeBedRepository extends JpaRepository<TypeBed, Long> {
    boolean existsByName(String name);

    List<TypeBed> findTypeBedByUseYn(Boolean useYn);

    default DataTableResults<ResponseTypeBedDTO> getDatatable(VfData vfData, RequestTypeBedDTO request) {
        List<Object> paramList = new ArrayList<>();
        String strSql = "SELECT" + " tb.id as id," +
                " tb.name as name," +
                " tb.use_yn as useYN," +
                " tb.create_date as createDate," +
                " tb.update_date as updateDate" +
                " FROM type_bed tb";
        StringBuilder strCondition = new StringBuilder("    WHERE 1 = 1");
        if (!CommonUtils.isNullOrEmpty(request.getName())){
            CommonUtils.filter(request.getName(), strCondition, paramList, "tb.name");
        }
        if (!CommonUtils.isEmpty(request.getUseYN())){
            CommonUtils.filter(request.getUseYN(), strCondition, paramList, "tb.use_yn");
        }
        String orderBy = "  ORDER BY tb.create_date DESC";
        return vfData.findPaginationQuery(strSql + strCondition.toString(), orderBy, paramList, ResponseTypeBedDTO.class);
    }
}
