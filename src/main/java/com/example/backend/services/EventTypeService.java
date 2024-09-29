package com.example.backend.services;

import com.example.backend.domain.request.EventTypeRequestDTO;
import com.example.backend.domain.response.EventTypeResponseDTO;
import com.example.backend.entity.EventType;
import com.example.backend.utils.enums.EventTypes;

import java.util.List;

public interface EventTypeService {
    List<EventTypeResponseDTO> getListEventByType(EventTypes eventTypes);

    void saveOrUpdate(EventTypeRequestDTO request);

    void delete(Long id);
}
