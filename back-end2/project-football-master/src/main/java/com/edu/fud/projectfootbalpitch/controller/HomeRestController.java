package com.edu.fud.projectfootbalpitch.controller;

import com.edu.fud.projectfootbalpitch.dto.*;
import com.edu.fud.projectfootbalpitch.response.Main;
import com.edu.fud.projectfootbalpitch.response.ServiceResponse;
import com.edu.fud.projectfootbalpitch.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@RestController
public class HomeRestController {
    Main main = new Main();
    @Autowired
    FootbalPitchService footballPitchService;
    @Autowired
    PitchTypeService pitchTypeService;
    @Autowired
    SubPitchService subPitchService;
    @Autowired
    FootballPitchScheduleService footballPitchScheduleService;
    @Autowired
    PitchScheduleServiceService pitchScheduleServiceService;
    @Autowired
    BookFootballPitchService bookFootballPitchService;
    @Autowired
    ServiceService service;
    @Autowired
    UserService userService;

    @GetMapping("/getListService")
    public ResponseEntity<Object> getListService() {
        System.err.println("test");
        List<ServiceDto> list = service.findAll();
        ServiceResponse<List<ServiceDto>> response = new ServiceResponse<>("success", list);
        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }
    // update booking service
    @PostMapping("/edit-booking-service/{scheduleId}")
    public ResponseEntity<Object> addUpdateBookingService(
            @PathVariable("scheduleId") long scheduleId,
            @RequestBody List<PitchScheduleServiceDto> serviceDtoList) {
        double sumOld = pitchScheduleServiceService.sumOldScheduleService(scheduleId);
        double sumNew, sumAdd = 0;
        for (PitchScheduleServiceDto dto : serviceDtoList) {
            if (dto.getQuantity() != 0) {
                dto.setFootballPitchScheduleId(scheduleId);
                Optional<ServiceDto> serviceDtos = service.findById(dto.getServicePitchId());
                dto.setPrice(serviceDtos.get().getPrice());
                PitchScheduleServiceDto oldDto = new PitchScheduleServiceDto();
                if (pitchScheduleServiceService.findOnePitchScheduleServiceByScheduleIdAndServiceId(scheduleId, dto.getServicePitchId()) != null) {
                    oldDto = pitchScheduleServiceService.findOnePitchScheduleServiceByScheduleIdAndServiceId(scheduleId, dto.getServicePitchId());
                } else {
                    pitchScheduleServiceService.save(dto);
                    sumNew = pitchScheduleServiceService.sumOldScheduleService(scheduleId);
                    sumAdd = sumNew - sumOld;
                }
                if (oldDto.getQuantity() <= dto.getQuantity()) {
                    if (pitchScheduleServiceService.pitchScheduleServiceId(scheduleId, dto.getServicePitchId()) != null) {
                        dto.setId(pitchScheduleServiceService.pitchScheduleServiceId(scheduleId, dto.getServicePitchId()));
                        pitchScheduleServiceService.save(dto);
                        sumNew = pitchScheduleServiceService.sumOldScheduleService(scheduleId);
                        sumAdd = sumNew - sumOld;
                    } else {
                        pitchScheduleServiceService.save(dto);
                        sumNew = pitchScheduleServiceService.sumOldScheduleService(scheduleId);
                        sumAdd = sumNew - sumOld;
                    }
                } else {
                    pitchScheduleServiceService.save(oldDto);
                }
            } else {
                continue;
            }
        }

        ServiceResponse<Double> response = new ServiceResponse<>("success", sumAdd);
        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }

    @GetMapping("/findPitchByDistrict")
    public ResponseEntity<Object> findPitchByDistrict(@RequestParam("app") long id) {
        List<FootBallPitchDto> list = footballPitchService.findPitchByDistrictId(id);
        ServiceResponse<List<FootBallPitchDto>> response = new ServiceResponse<>("success", list);
        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }


    @GetMapping("/findPitchByWard")
    public ResponseEntity<Object> findPitchByWard(@RequestParam("app") long id, @RequestParam("app1") long id1) {
        if (id == 0) {
            List<FootBallPitchDto> list = footballPitchService.findPitchByDistrictId(id1);
            ServiceResponse<List<FootBallPitchDto>> response = new ServiceResponse<>("success", list);
            return new ResponseEntity<Object>(response, HttpStatus.OK);
        } else {
            List<FootBallPitchDto> list = footballPitchService.findPitchByWardId(id);
            ServiceResponse<List<FootBallPitchDto>> response = new ServiceResponse<>("success", list);
            return new ResponseEntity<Object>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/findSubPitchByType")
    public ResponseEntity<Object> findSubPitchByType(@RequestParam("pitchType") long pitchType, @RequestParam("pitchId") long pitchId) {
        if (pitchType==0){
            List<SubPitchDto> list = subPitchService.findAllSubPitchByPitchId(pitchId);
            ServiceResponse<List<SubPitchDto>> response = new ServiceResponse<>("success", list);
            return new ResponseEntity<Object>(response, HttpStatus.OK);
        }else{
            List<SubPitchDto> list = subPitchService.findAllSubPitchByPitchTypeId(pitchId, pitchType);
            ServiceResponse<List<SubPitchDto>> response = new ServiceResponse<>("success", list);
            return new ResponseEntity<Object>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/checkDateBooking")
    public ResponseEntity<Object> checkDateBooking(@RequestParam("dateBooking") Date dateBooking,
                                                   @RequestParam("subPitchId") long subPitchId,
                                                   @RequestParam("timeStart") String timeStartInput,
                                                   @RequestParam("timeEnd") String timeEndInput) throws ParseException {
        List<FootballPitchScheduleDto> list = footballPitchScheduleService.findAllByDateBooking(dateBooking, subPitchId);
        if (timeStartInput == "" && timeEndInput == "") {
            ServiceResponse<List<FootballPitchScheduleDto>> response = new ServiceResponse<>("success", list);
            return new ResponseEntity<Object>(response, HttpStatus.OK);
        } else {
            String check = main.checkTimeByList(main.covertStringToTime(timeStartInput),
                    main.covertStringToTime(timeEndInput), list);
            if (list.size() == 0) {
                FootballPitchScheduleDto dto = new FootballPitchScheduleDto();
                dto.setMessage(check);
                list.add(dto);
                ServiceResponse<List<FootballPitchScheduleDto>> response = new ServiceResponse<>("success", list);
                return new ResponseEntity<Object>(response, HttpStatus.OK);
            } else {
                System.out.println(check);
                for (FootballPitchScheduleDto dto : list) {
                    dto.setMessage(check);
                }
                ServiceResponse<List<FootballPitchScheduleDto>> response = new ServiceResponse<>("success", list);
                return new ResponseEntity<Object>(response, HttpStatus.OK);
            }
        }
    }


}
