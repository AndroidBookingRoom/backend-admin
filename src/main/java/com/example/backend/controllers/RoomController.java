package com.example.backend.controllers;

import com.example.backend.domain.Response;
import com.example.backend.domain.request.RequestRoomDTO;
import com.example.backend.domain.request.RequestTypeRoomDTO;
import com.example.backend.services.RoomService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/v1/room")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomController {
    final RoomService roomService;
    @RequestMapping(path = "/saveOrUpdate", method = RequestMethod.POST)
    public @ResponseBody Response typeRoomSaveOrUpdate(@Valid RequestRoomDTO request) {
        roomService.saveOrUpdate(request);
        return Response.success();
    }

    @RequestMapping(path = "/search", method = RequestMethod.GET)
    public @ResponseBody Response getListRoom(RequestRoomDTO request){
        return Response.success().withData(roomService.getDataTables(request));
    }

}
