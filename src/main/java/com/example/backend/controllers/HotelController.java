package com.example.backend.controllers;

import com.example.backend.domain.Response;
import com.example.backend.domain.request.RequestHotelDTO;
import com.example.backend.domain.request.RequestRoomDTO;
import com.example.backend.services.HotelService;
import com.example.backend.utils.enums.ErrorCodes;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/v1/hotel")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class HotelController {
    HotelService hotelService;

    @RequestMapping(path = "/saveOrUpdate", method = RequestMethod.POST)
    public @ResponseBody Response saveOrUpdate(RequestHotelDTO request) {
        hotelService.saveOrUpdate(request);
        return Response.success(ErrorCodes.SUCCESS.getCode());
    }

    @RequestMapping(path = "/search", method = RequestMethod.GET)
    public @ResponseBody Response getListHotel(RequestHotelDTO request) {
        return Response.success().withData(hotelService.getDataTables(request));
    }

    @RequestMapping(path = "/find-by-id", method = RequestMethod.GET)
    public @ResponseBody Response findHotelById(@RequestParam Long id) {
        return Response.success().withData(hotelService.findHotelById(id));
    }
}
