package com.edu.fud.projectfootbalpitch.controller.ChatBoxController;


import com.edu.fud.projectfootbalpitch.dto.FootBallPitchDto;
import com.edu.fud.projectfootbalpitch.dto.ProductDto;
import com.edu.fud.projectfootbalpitch.dto.SubPitchDto;
import com.edu.fud.projectfootbalpitch.response.BaseError;
import com.edu.fud.projectfootbalpitch.response.BaseResponse;
import com.edu.fud.projectfootbalpitch.service.FootbalPitchService;
import com.edu.fud.projectfootbalpitch.service.ProductService;
import com.edu.fud.projectfootbalpitch.service.StatisticsService;
import com.edu.fud.projectfootbalpitch.service.SubPitchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/chatbox")
public class ChatboxController {

    @Autowired
    private FootbalPitchService footbalPitchService;

    @Autowired
    private ProductService productService;

    @Autowired
    private SubPitchService subPitchService;

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/footballPitchs")
    public ResponseEntity<BaseResponse<List<FootBallPitchDto>>> getAllFootBallPitch() {
        BaseResponse<List<FootBallPitchDto>> response = new BaseResponse<>();
        response.setSuccess(false);
        HttpStatus status = null;
        try {
            response.setSuccess(true);
            response.setData(footbalPitchService.findAllCuaVi());
        } catch (Exception e) {
            response.addError(new BaseError(HttpStatus.EXPECTATION_FAILED.toString(), HttpStatus.EXPECTATION_FAILED.toString()));
            status = HttpStatus.EXPECTATION_FAILED;
        }
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.status(status).body(response);
    }

    @GetMapping("/footballPitchByName")
    public ResponseEntity<BaseResponse<List<FootBallPitchDto>>> getAllFootBallPitchByName(
            @RequestParam("name") String name) {
        BaseResponse<List<FootBallPitchDto>> response = new BaseResponse<>();
        response.setSuccess(false);
        HttpStatus status = null;
        try {
            response.setSuccess(true);
            response.setData(footbalPitchService.findAllByName(name));
        } catch (Exception e) {
            response.addError(new BaseError(HttpStatus.EXPECTATION_FAILED.toString(), HttpStatus.EXPECTATION_FAILED.toString()));
            status = HttpStatus.EXPECTATION_FAILED;
        }
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.status(status).body(response);
    }

    @GetMapping("/footballPitchByAreas")
    public ResponseEntity<BaseResponse<List<FootBallPitchDto>>> getAllFootballPitchByWardOrDistrictName(
            @RequestParam("name") String name) {
        BaseResponse<List<FootBallPitchDto>> response = new BaseResponse<>();
        response.setSuccess(false);
        HttpStatus status = null;
        try {
            response.setSuccess(true);
            response.setData(footbalPitchService.findPitchByWardNameOrDistrictName(name));
        } catch (Exception e) {
            response.addError(new BaseError(HttpStatus.EXPECTATION_FAILED.toString(), HttpStatus.EXPECTATION_FAILED.toString()));
            status = HttpStatus.EXPECTATION_FAILED;
        }
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.status(status).body(response);
    }


    @GetMapping("/subPitchByDateAndTime")
    public ResponseEntity<BaseResponse<List<SubPitchDto>>> getAllSubPitchByDateAndTime(
            @RequestParam("date")Date dateBooking,
            @RequestParam("time")Time timeBooking) {
        BaseResponse<List<SubPitchDto>> response = new BaseResponse<>();
        response.setSuccess(false);
        HttpStatus status = null;
        try {
            response.setSuccess(true);
            response.setData(subPitchService.findSubPitchByDateAndTime(dateBooking,timeBooking));
        } catch (Exception e) {
            response.addError(new BaseError(HttpStatus.EXPECTATION_FAILED.toString(), HttpStatus.EXPECTATION_FAILED.toString()));
            status = HttpStatus.EXPECTATION_FAILED;
        }
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.status(status).body(response);
    }

    @GetMapping("/subPitchByAreasAndSpecTime")
    public ResponseEntity<BaseResponse<List<SubPitchDto>>> getAllSubPitchByAreasAndSpecTime(
            @RequestParam("keyword")String Keyword,
            @RequestParam("date")Date dateBooking,
            @RequestParam("time")Time timeBooking) {
        BaseResponse<List<SubPitchDto>> response = new BaseResponse<>();
        response.setSuccess(false);
        HttpStatus status = null;
        try {
            response.setSuccess(true);
            response.setData(subPitchService.findSubPitchByAreasAndSpecTime(Keyword,dateBooking,timeBooking));
        } catch (Exception e) {
            response.addError(new BaseError(HttpStatus.EXPECTATION_FAILED.toString(), HttpStatus.EXPECTATION_FAILED.toString()));
            status = HttpStatus.EXPECTATION_FAILED;
        }
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.status(status).body(response);
    }


    /////////////////////////////////////////////////////////////////////////////
    //PRODUCT
    /////////////////////////////////////////////////////////////////////////////
    @GetMapping("/products")
    public ResponseEntity<BaseResponse<List<ProductDto>>> getAllProduct() {
        BaseResponse<List<ProductDto>> response = new BaseResponse<>();
        response.setSuccess(false);
        HttpStatus status = null;
//        RestTemplate restTemplate = new RestTemplate();
//        RestTemplate restTemplate1 = new RestTemplate();
//        RestTemplate restTemplate2 = new RestTemplate();
//        RestTemplate restTemplate3 = new RestTemplate();
//        RestTemplate restTemplate4 = new RestTemplate();
//        restTemplate.exchange()
        try {
            response.setSuccess(true);
            response.setData(productService.findAll());
        } catch (Exception e) {
            response.addError(new BaseError(HttpStatus.EXPECTATION_FAILED.toString(), HttpStatus.EXPECTATION_FAILED.toString()));
            status = HttpStatus.EXPECTATION_FAILED;
        }
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.status(status).body(response);
    }
    @GetMapping("/productByNameOrCate")
    public ResponseEntity<BaseResponse<List<ProductDto>>> getAllProductByNameOrCate(
            @RequestParam("name") String name) {
        BaseResponse<List<ProductDto>> response = new BaseResponse<>();
        response.setSuccess(false);
        HttpStatus status = null;
        try {
            response.setSuccess(true);
            response.setData(productService.findAllProductByNameOrCate(name));
        } catch (Exception e) {
            response.addError(new BaseError(HttpStatus.EXPECTATION_FAILED.toString(), HttpStatus.EXPECTATION_FAILED.toString()));
            status = HttpStatus.EXPECTATION_FAILED;
        }
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.status(status).body(response);
    }

    @GetMapping("/productSoldMost")
    public ResponseEntity<BaseResponse<List<ProductDto>>> getFiveProductSoldMost(
            ) {
        BaseResponse<List<ProductDto>> response = new BaseResponse<>();
        response.setSuccess(false);
        HttpStatus status = null;
        try {
            response.setSuccess(true);
            response.setData(statisticsService.findAllTop5ProductSell());
        } catch (Exception e) {
            response.addError(new BaseError(HttpStatus.EXPECTATION_FAILED.toString(), HttpStatus.EXPECTATION_FAILED.toString()));
            status = HttpStatus.EXPECTATION_FAILED;
        }
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.status(status).body(response);
    }

    @GetMapping("/productLastCreateDate")
    public ResponseEntity<BaseResponse<List<ProductDto>>> getFiveProductLastCreateDate(
    ) {
        BaseResponse<List<ProductDto>> response = new BaseResponse<>();
        response.setSuccess(false);
        HttpStatus status = null;
        try {
            response.setSuccess(true);
            response.setData(productService.findFiveProductByLastCreateDate());
        } catch (Exception e) {
            response.addError(new BaseError(HttpStatus.EXPECTATION_FAILED.toString(), HttpStatus.EXPECTATION_FAILED.toString()));
            status = HttpStatus.EXPECTATION_FAILED;
        }
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.status(status).body(response);
    }

}
