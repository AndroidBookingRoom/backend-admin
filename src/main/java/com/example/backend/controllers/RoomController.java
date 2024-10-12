package com.example.backend.controllers;

import com.example.backend.domain.Response;
import com.example.backend.domain.request.RequestRoomDTO;
import com.example.backend.services.RoomService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @RequestMapping(path = "/find-by-id", method = RequestMethod.GET)
    public @ResponseBody Response findRoomByHotel(@RequestParam Long id) {
        return Response.success().withData(roomService.findRoomById(id));
    }

    @RequestMapping(path = "/deletes", method = RequestMethod.DELETE)
    public @ResponseBody Response deleteListRoom(@RequestParam List<Long> ids){
        roomService.deleteRoomByListId(ids);
        return Response.success();
    }

}
