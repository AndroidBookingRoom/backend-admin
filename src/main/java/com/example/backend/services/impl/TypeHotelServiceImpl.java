package com.example.backend.services.impl;

import com.example.backend.common.CommonUtils;
import com.example.backend.common.DataTableResults;
import com.example.backend.common.VfData;
import com.example.backend.domain.request.RequestTypeHotelDTO;
import com.example.backend.domain.response.ResponseTypeHotelDTO;
import com.example.backend.entity.TypeHotel;
import com.example.backend.repositorys.TypeHotelRepository;
import com.example.backend.services.TypeHotelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TypeHotelServiceImpl implements TypeHotelService {
    final VfData vfData;
    final TypeHotelRepository typeHotelRepository;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(RequestTypeHotelDTO request) {
        if (CommonUtils.isEmpty(request.getId()))
            saveTypeHotel(request);
        else
            updateTypeHotel(request);
    }

    @Override
    public DataTableResults<ResponseTypeHotelDTO> getDataTables(RequestTypeHotelDTO request) {
        log.info("[TYPE HOTEL SERVICE IMPL] getDataTables with request:{}", CommonUtils.convertObjectToStringJson(request));
        return typeHotelRepository.getDatatable(vfData, request);
    }

    @Override
    public void deletesTypeHotel(List<Long> ids) {
        checkTypeHotelDelete();
        for (Long id : ids) {
            if (typeHotelRepository.findById(id).isPresent()){
                typeHotelRepository.deleteById(id);
            }else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Not Found TypeBed with id: %s", id));
            }
        }
    }

    @Override
    public List<ResponseTypeHotelDTO> getListTypeHotelActive() {
        List<TypeHotel> typeHotels = typeHotelRepository.getTypeHotelByUseYn(Boolean.TRUE);
        if (CommonUtils.isNullOrEmpty(typeHotels)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found TypeHotel active");
        }
        List<ResponseTypeHotelDTO> response = new ArrayList<>();
        for (TypeHotel typeHotel : typeHotels) {
            response.add(ResponseTypeHotelDTO.builder()
                            .id(typeHotel.getId())
                            .name(typeHotel.getName())
                    .build());
        }

        return response;
    }

    private void checkTypeHotelDelete() {}

    private void saveTypeHotel(RequestTypeHotelDTO request) {
        if (typeHotelRepository.existsByName(request.getName()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format("Đã tồn tại tên loại khách sạn: %s", request.getName()));
        TypeHotel typeHotel = TypeHotel.builder()
                .name(request.getName())
                .useYn(request.getUseYN())
                .build();
        typeHotelRepository.save(typeHotel);
    }

    private void updateTypeHotel(RequestTypeHotelDTO request) {
        if (typeHotelRepository.existsByNameAndIdNotIn(request.getName(), List.of(request.getId())))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    String.format("Đã tồn tại tên loại khách sạn: %s", request.getName()));
        TypeHotel typeHotel = typeHotelRepository.findById(request.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("Không tìm thấy loại khách sạn với id:%s", request.getId())));
        typeHotel.setName(request.getName());
        typeHotel.setUseYn(request.getUseYN());
        typeHotelRepository.save(typeHotel);
    }
}
