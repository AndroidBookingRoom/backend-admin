package com.example.backend.controllers;

import com.example.backend.domain.Response;
import com.example.backend.domain.request.RequestHotelDTO;
import com.example.backend.services.HotelService;
import com.example.backend.utils.enums.ErrorCodes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/v1/hotel")
@RequiredArgsConstructor
public class HotelController {
    private final HotelService hotelService;
    @RequestMapping(path = "/saveOrUpdate",method = RequestMethod.POST)
    public @ResponseBody Response saveOrUpdate(RequestHotelDTO request){
        hotelService.saveOrUpdate(request);
        return Response.success(ErrorCodes.SUCCESS.getCode());
    }
}
