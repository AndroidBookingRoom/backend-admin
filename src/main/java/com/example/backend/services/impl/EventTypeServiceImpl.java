package com.example.backend.services.impl;

import com.example.backend.common.CommonUtils;
import com.example.backend.domain.request.EventTypeRequestDTO;
import com.example.backend.domain.response.EventTypeResponseDTO;
import com.example.backend.entity.EventType;
import com.example.backend.repositorys.EventTypeRepository;
import com.example.backend.services.EventTypeService;
import com.example.backend.utils.enums.EventTypes;
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
public class EventTypeServiceImpl implements EventTypeService {
    private final EventTypeRepository eventTypeRepository;

    @Override
    public List<EventTypeResponseDTO> getListEventByType(EventTypes eventTypes) {
        List<EventTypeResponseDTO> response = new ArrayList<>();
        List<EventType> lstEvent = eventTypeRepository.findByEventTypes(eventTypes);
        for (EventType eventType : lstEvent) {
            response.add(EventTypeResponseDTO.builder()
                    .id(eventType.getId())
                    .idCombo(eventType.getIdCombo())
                    .idTour(eventType.getIdTour())
                    .eventName(eventType.getEventName())
                    .useYN(eventType.getUseYN())
                    .eventTypes(eventType.getEventTypes())
                    .build());
        }
        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(EventTypeRequestDTO request) {
        EventType eventType = buildEventType(request);
        if (!CommonUtils.isEmpty(request.getId())) {
            EventType event = eventTypeRepository.findById(request.getId()).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Không tìm thấy event với id:%s", request.getId())));
            if (!event.getEventName().equals(request.getEventName())
                    && eventTypeRepository.existsAllByEventName(request.getEventName())){
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        String.format("Đã tồn tại event name :%s", request.getEventName()));
            }
            //Face update
            if (!CommonUtils.isEmpty(request.getUseYN())) {
                eventType.setUseYN(request.getUseYN());
            }
            eventType.setUseYN(CommonUtils.isEmpty(request.getUseYN()) ? event.getUseYN() : request.getUseYN());
            eventTypeRepository.save(eventType);
            return;
        }
        if (eventTypeRepository.existsAllByEventName(request.getEventName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    String.format("Đã tồn tại event name :%s", request.getEventName()));
        }
        eventTypeRepository.save(eventType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        EventType event = eventTypeRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("Không tìm thấy event với id:%s", id)));
        eventTypeRepository.delete(event);
    }

    private EventType buildEventType(EventTypeRequestDTO request) {
        return EventType.builder()
                .id(request.getId())
                .idTour(request.getIdTour())
                .idCombo(request.getIdCombo())
                .eventName(request.getEventName())
                .eventTypes(request.getEventTypes())
                .build();
    }
}
