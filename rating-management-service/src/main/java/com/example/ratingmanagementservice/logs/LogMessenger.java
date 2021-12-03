package com.example.ratingmanagementservice.logs;

import com.google.gson.Gson;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class LogMessenger {

    @Autowired
    private EurekaClient eurekaClient;

    private String getBaseUrl(){
        Application application = eurekaClient.getApplication("log-management");
        if(application == null || application.getInstances().isEmpty()){
            return null;
        }
        InstanceInfo instanceInfo = application.getInstances().get(0);
        return "http://" + instanceInfo.getHostName() + ":" + instanceInfo.getPort();
    }

    public void send(List<LogCollector.Log> logList){
        Gson gson = new Gson();
        String payload = gson.toJson(logList);

        if(getBaseUrl() == null){
            return;
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(getBaseUrl() + "/add"))
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();

        HttpClient client = HttpClient.newHttpClient();

        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException e){
            System.out.println("interrupted");
        } catch (IOException e){
            System.out.println("IO exception");
        }
    }

}
