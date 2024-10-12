package com.example.backend.controllers;

import com.example.backend.common.DataTableResults;
import com.example.backend.domain.Response;
import com.example.backend.domain.SearchParams;
import com.example.backend.domain.request.RequestTypeBedDTO;
import com.example.backend.domain.request.RequestTypeHotelDTO;
import com.example.backend.domain.request.RequestTypeRoomDTO;
import com.example.backend.domain.response.ResponseTypeRoomDTO;
import com.example.backend.services.TypeBedService;
import com.example.backend.services.TypeHotelService;
import com.example.backend.services.TypeRoomService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/v1/config")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ConfigController {
    final TypeRoomService typeRoomService;
    final TypeBedService typeBedService;
    final TypeHotelService typeHotelService;

    @RequestMapping(path = "/type-room/saveOrUpdate", method = RequestMethod.POST)
    public @ResponseBody Response typeRoomSaveOrUpdate(@Valid RequestTypeRoomDTO request) {
        typeRoomService.saveOrUpdate(request);
        return Response.success();
    }

    @RequestMapping(path = "/type-room/search", method = RequestMethod.GET)
    public @ResponseBody Response getListTypeRoom(RequestTypeRoomDTO request){
        return Response.success().withData(typeRoomService.getDataTables(request));
    }

    @RequestMapping(path = "/type-room/deletes", method = RequestMethod.DELETE)
    public @ResponseBody Response deleteTypeRoom(@RequestParam List<Long> ids){
        typeRoomService.deletesTypeRoom(ids);
        return Response.success();
    }

    @RequestMapping(path = "/type-room/getList", method = RequestMethod.GET)
    public @ResponseBody Response getListTypeRoom(){
        return Response.success().withData(typeRoomService.getListTypeRoomActive());
    }

    @RequestMapping(path = "/type-room/find-by-hotel", method = RequestMethod.GET)
    public @ResponseBody Response findTypeRoomByHotel(@RequestParam Long id){
        return Response.success().withData(typeRoomService.getListTypeHotelActiveByHotelId(id));
    }


    @RequestMapping(path = "/type-bed/saveOrUpdate", method = RequestMethod.POST)
    public @ResponseBody Response typeBedSaveOrUpdate(@Valid RequestTypeBedDTO request) {
        typeBedService.saveOrUpdate(request);
        return Response.success();
    }

    @RequestMapping(path = "/type-bed/search", method = RequestMethod.GET)
    public @ResponseBody Response getListTypeBed(RequestTypeBedDTO request){
        return Response.success().withData(typeBedService.getDataTables(request));
    }

    @RequestMapping(path = "/type-bed/deletes", method = RequestMethod.DELETE)
    public @ResponseBody Response deleteTypeBed(@RequestParam List<Long> ids){
        typeBedService.deletesTypeRoom(ids);
        return Response.success();
    }

    @RequestMapping(path = "/type-bed/getList", method = RequestMethod.GET)
    public @ResponseBody Response getListTypeBed(){
        return Response.success().withData(typeBedService.getListTypeBedActive());
    }

    @RequestMapping(path = "/type-hotel/saveOrUpdate", method = RequestMethod.POST)
    public @ResponseBody Response typeHotelSaveOrUpdate(@Valid RequestTypeHotelDTO request) {
        typeHotelService.saveOrUpdate(request);
        return Response.success();
    }

    @RequestMapping(path = "/type-hotel/search", method = RequestMethod.GET)
    public @ResponseBody Response getListTypeHotel(RequestTypeHotelDTO request){
        return Response.success().withData(typeHotelService.getDataTables(request));
    }

    @RequestMapping(path = "/type-hotel/deletes", method = RequestMethod.DELETE)
    public @ResponseBody Response deleteTypeHotel(@RequestParam List<Long> ids){
        typeHotelService.deletesTypeHotel(ids);
        return Response.success();
    }

    @RequestMapping(path = "/type-hotel/getList", method = RequestMethod.GET)
    public @ResponseBody Response getListTypeHotel(){
        return Response.success().withData(typeHotelService.getListTypeHotelActive());
    }

    @RequestMapping(path = "/type-hotel/find-by-hotel", method = RequestMethod.GET)
    public @ResponseBody Response getListTypeHotel(@RequestParam Long id){
        return Response.success().withData(typeHotelService.getListTypeHotelActiveByHotelId(id));
    }
}
