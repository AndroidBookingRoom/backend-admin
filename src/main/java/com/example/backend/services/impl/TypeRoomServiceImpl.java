package com.example.backend.services.impl;

import com.example.backend.common.CommonUtils;
import com.example.backend.common.DataTableResults;
import com.example.backend.common.VfData;
import com.example.backend.domain.request.RequestTypeRoomDTO;
import com.example.backend.domain.response.ResponseTypeRoomDTO;
import com.example.backend.entity.TypeRoom;
import com.example.backend.repositorys.TypeRoomRepository;
import com.example.backend.services.TypeRoomService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TypeRoomServiceImpl implements TypeRoomService {
    final TypeRoomRepository typeRoomRepository;

    final VfData vfData;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(RequestTypeRoomDTO request) {
        if (CommonUtils.isEmpty(request.getId()))
            saveTypeRoom(request);
        else
            updateTypeRoom(request);
    }

    @Override
    public DataTableResults<ResponseTypeRoomDTO> getDataTables(RequestTypeRoomDTO request) {
        log.info("[TYPE ROOM SERVICE IMPL] getDataTables with request:{}", CommonUtils.convertObjectToStringJson(request));
        return typeRoomRepository.getDatatable(vfData, request);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletesTypeRoom(List<Long> ids) {
        checkTypeRoomDelete();
        for (Long id : ids) {
            if (typeRoomRepository.findById(id).isPresent()){
                typeRoomRepository.deleteById(id);
            }else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Not Found TypeRoom with id: %s", id));
            }
        }
    }

    private void checkTypeRoomDelete(){

    }

    private void updateTypeRoom(RequestTypeRoomDTO request) {
        TypeRoom typeRoom = typeRoomRepository.findById(request.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("Không tìm thấy Type Room với id:%s", request.getId())));
        typeRoom.setName(request.getName());
        typeRoom.setUseYn(request.getUseYN());
        typeRoomRepository.save(typeRoom);
    }

    private void saveTypeRoom(RequestTypeRoomDTO request) {
        if (typeRoomRepository.existsByName(request.getName()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format("Đã tồn tại tên loại phòng: %s", request.getName()));
        TypeRoom typeRoom = TypeRoom.builder()
                .name(request.getName())
                .useYn(request.getUseYN())
                .build();
        typeRoomRepository.save(typeRoom);
    }
}
