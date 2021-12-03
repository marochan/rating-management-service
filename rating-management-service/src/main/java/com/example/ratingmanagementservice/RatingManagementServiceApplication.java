package com.example.ratingmanagementservice;

import com.example.ratingmanagementservice.controller.RatingManagementServiceController;
import com.example.ratingmanagementservice.logs.LogCollector;
import com.example.ratingmanagementservice.logs.UtilConst;
import com.google.gson.Gson;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableDiscoveryClient
@CrossOrigin
@ComponentScan("com.example.ratingmanagementservice.controller")
public class RatingManagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RatingManagementServiceApplication.class, args);
    }

    @Autowired
    private RatingManagementServiceController controller;

    @GetMapping("/search")
    public Object search(
            @RequestParam(value = "searchPhrase") String searchPhrase
    ) {
        //LogCollector collector = LogCollector.getInstance();
        try {
            //collector.addLog(UtilConst.LOGLEVEL_INFO, null, "Received search request for phrase: " + searchPhrase);
            //RatingManagementServiceController controller = new RatingManagementServiceController();
            //collector.release();
            return controller.search(searchPhrase);
        } catch (Exception e){
            //collector.addLog(UtilConst.LOGLEVEL_EXCEPTION, e.getStackTrace().toString(), e.getMessage());
            //collector.release();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return e.getMessage()+sw.toString();
        }
    }


    @PostMapping("/add")
    public Object add(
            @RequestParam(value = "name") String name,
            @RequestParam(value = "year") String year
    ) {
        //LogCollector collector = LogCollector.getInstance();
        try {
            //RatingManagementServiceController controller = new RatingManagementServiceController();
            controller.add(name, year);
            //collector.release();
            return ResponseEntity.ok().build();
        } catch (Exception e){
            //collector.addLog(UtilConst.LOGLEVEL_EXCEPTION, e.getStackTrace().toString(), e.getMessage());
            //collector.release();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return e.getMessage()+sw.toString();
        }
    }

    @PostMapping("/rate")
    public Object rate(
            @RequestParam(value = "token") String token,
            @RequestParam(value = "movieId") String movieId,
            @RequestParam(value = "cat1") String cat1,
            @RequestParam(value = "cat2") String cat2,
            @RequestParam(value = "cat3") String cat3,
            @RequestParam(value = "cat4") String cat4,
            @RequestParam(value = "cat5") String cat5,
            @RequestParam(value = "cat6") String cat6
    ) {
        LogCollector collector = LogCollector.getInstance();
        try {
            //RatingManagementServiceController controller = new RatingManagementServiceController();
            controller.rate(token, movieId, cat1, cat2, cat3, cat4, cat5, cat6);
            collector.release();
            return ResponseEntity.ok().build();
        } catch (Exception e){
            collector.addLog(UtilConst.LOGLEVEL_EXCEPTION, e.getStackTrace().toString(), e.getMessage());
            collector.release();
            return new ResponseEntity<>(
                    e.getMessage()+e.getStackTrace(),
                    HttpStatus.BAD_REQUEST);
        }
    }


    private String serialize(Object o){
        Gson gson = new Gson();
        return gson.toJson(o);
    }
}