package com.example.ratingmanagementservice.controller;

import com.example.ratingmanagementservice.logs.LogCollector;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Component
@CrossOrigin
public class RatingManagementServiceController {

    @Autowired
    private EurekaClient eurekaClient;


    private static LogCollector logCollector;

    private String getBaseUrl(){
        Application application = eurekaClient.getApplication("data-access");
        if(application == null || application.getInstances().isEmpty()){
            return null;
        }
        InstanceInfo instanceInfo = application.getInstances().get(0);
        return "http://" + instanceInfo.getHostName() + ":" + instanceInfo.getPort();
    }

    public String search(String searchPhrase){

        return send("/search?searchPhrase="+searchPhrase, null).body();
    }

    public void add(String name, String year){
        send("/addmovie?name="+name+"&year="+year, null);
    }

    public void rate(String token, String movieId, String cat1, String cat2, String cat3, String cat4, String cat5, String cat6){
        String path = String.format("/updaterating?token=%s&movieId=%s&cat1=%s&cat2=%s&cat3=%s&cat4=%s&cat5=%s&cat6=%s",
                token, movieId, cat1, cat2, cat3, cat4, cat5, cat6);
        send(path, null);
    }

    private HttpResponse<String> send(String path, String body){
        if(getBaseUrl() == null){
            return null;
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(getBaseUrl() + path))
                .GET()
                .build();

        HttpClient client = HttpClient.newHttpClient();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response;
        } catch (InterruptedException e){
            System.out.println("interrupted");
        } catch (IOException e){
            System.out.println("IO exception");
        }
        return null;
    }

}