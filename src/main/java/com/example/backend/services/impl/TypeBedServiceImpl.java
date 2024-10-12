package com.example.backend.services.impl;

import com.example.backend.common.CommonUtils;
import com.example.backend.common.DataTableResults;
import com.example.backend.common.VfData;
import com.example.backend.domain.request.RequestTypeBedDTO;
import com.example.backend.domain.request.RequestTypeRoomDTO;
import com.example.backend.domain.response.ResponseTypeBedDTO;
import com.example.backend.domain.response.ResponseTypeRoomDTO;
import com.example.backend.entity.TypeBed;
import com.example.backend.entity.TypeRoom;
import com.example.backend.repositorys.TypeBedRepository;
import com.example.backend.services.TypeBedService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class TypeBedServiceImpl implements TypeBedService {
    final TypeBedRepository typeBedRepository;
    final VfData vfData;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(RequestTypeBedDTO request) {
        if (CommonUtils.isEmpty(request.getId()))
            saveTypeBed(request);
        else
            updateTypeBed(request);
    }

    @Override
    public DataTableResults<ResponseTypeBedDTO> getDataTables(RequestTypeBedDTO request) {
        log.info("[TYPE BED SERVICE IMPL] getDataTables with request:{}", CommonUtils.convertObjectToStringJson(request));
        return typeBedRepository.getDatatable(vfData, request);
    }

    @Override
    public List<ResponseTypeBedDTO> getListTypeBedActive() {
        List<TypeBed> listTypeBed = typeBedRepository.findTypeBedByUseYn(Boolean.TRUE);
        if (CommonUtils.isNullOrEmpty(listTypeBed)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found TypeBed active");
        }
        List<ResponseTypeBedDTO> response = new ArrayList<>();
        for (TypeBed typeBed : listTypeBed) {
            response.add(ResponseTypeBedDTO.builder()
                    .id(typeBed.getId())
                    .name(typeBed.getName())
                    .build());
        }
        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletesTypeRoom(List<Long> ids) {
        checkTypeBedDelete();
        for (Long id : ids) {
            if (typeBedRepository.findById(id).isPresent()){
                typeBedRepository.deleteById(id);
            }else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Not Found TypeBed with id: %s", id));
            }
        }
    }


    private void checkTypeBedDelete() {}

    private void saveTypeBed(RequestTypeBedDTO request) {
        if (typeBedRepository.existsByName(request.getName()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format("Đã tồn tại tên loại giường: %s", request.getName()));
        TypeBed typeBed = TypeBed.builder()
                .name(request.getName())
                .useYn(request.getUseYN())
                .build();
        typeBedRepository.save(typeBed);
    }

    private void updateTypeBed(RequestTypeBedDTO request) {
        TypeBed typeBed = typeBedRepository.findById(request.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("Không tìm thấy loại giường với id:%s", request.getId())));
        typeBed.setName(request.getName());
        typeBed.setUseYn(request.getUseYN());
        typeBedRepository.save(typeBed);
    }
}
