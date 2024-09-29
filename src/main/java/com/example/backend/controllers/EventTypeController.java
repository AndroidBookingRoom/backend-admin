package com.example.backend.controllers;

import com.example.backend.domain.Response;
import com.example.backend.domain.request.EventTypeRequestDTO;
import com.example.backend.domain.response.EventTypeResponseDTO;
import com.example.backend.services.EventTypeService;
import com.example.backend.utils.enums.EventTypes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/v1/event-type")
@RequiredArgsConstructor
public class EventTypeController {
    private final EventTypeService eventTypeService;

    @RequestMapping(path = "/combo/list", method = RequestMethod.GET)
    public @ResponseBody Response getListComboEvent() {
        List<EventTypeResponseDTO> response = eventTypeService.getListEventByType(EventTypes.COMBO);
        return Response.success().withData(response);
    }

    @RequestMapping(path = "/tour/list", method = RequestMethod.GET)
    public @ResponseBody Response getListTourEvent() {
        List<EventTypeResponseDTO> response = eventTypeService.getListEventByType(EventTypes.TOUR);
        return Response.success().withData(response);
    }

    @RequestMapping(path = "/saveOrUpdate", method = RequestMethod.POST)
    public @ResponseBody Response saveOrUpdate(@Valid EventTypeRequestDTO request) {
        eventTypeService.saveOrUpdate(request);
        return Response.success();
    }

    @RequestMapping(path = "/delete/{id}",method = RequestMethod.DELETE)
    public @ResponseBody Response deleteEvent(@PathVariable Long id){
        eventTypeService.delete(id);
        return Response.success();
    }
}
