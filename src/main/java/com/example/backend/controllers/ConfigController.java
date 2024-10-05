package com.example.backend.controllers;

import com.example.backend.common.DataTableResults;
import com.example.backend.domain.Response;
import com.example.backend.domain.SearchParams;
import com.example.backend.domain.request.RequestTypeRoomDTO;
import com.example.backend.domain.response.ResponseTypeRoomDTO;
import com.example.backend.services.TypeRoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/v1/config")
@RequiredArgsConstructor
public class ConfigController {
    private final TypeRoomService typeRoomService;

    @RequestMapping(path = "/type-room/saveOrUpdate", method = RequestMethod.POST)
    public @ResponseBody Response typeRoomSaveOrUpdate(@Valid RequestTypeRoomDTO request) {
        typeRoomService.saveOrUpdate(request);
        return Response.success();
    }

    @RequestMapping(path = "/type-room", method = RequestMethod.GET)
    public @ResponseBody Response getListTypeRoom(RequestTypeRoomDTO request){
        return Response.success().withData(typeRoomService.getDataTables(request));
    }
}
