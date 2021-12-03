package com.example.ratingmanagementservice.logs;

import java.io.PrintStream;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class LogCollector{

    public class Log{
        private String origin;
        private String type;
        private String stackTrace;
        private String message;
        private Timestamp timestamp;

        public Log(String type, String stackTrace, String message){
            this.type = type;
            this.stackTrace = stackTrace;
            this.message = message;
            this.timestamp = new Timestamp(System.currentTimeMillis());
            this.origin = UtilConst.LOG_ORIGIN;
        }

    }

    private List<Log> logs = new ArrayList<Log>();
    private static LogCollector instance;

    private LogCollector(){}

    public static LogCollector getInstance(){
        if(instance == null){
            instance = new LogCollector();
        }
        return instance;
    }

    public void addLog(String type, String stackTrace, String message){

        if(stackTrace == null){
            stackTrace = String.valueOf(Thread.currentThread().getStackTrace());
        }

        logs.add(new Log(type, stackTrace, message));
    }

    public void release(){
        LogMessenger messenger = new LogMessenger();

        messenger.send(logs);

        logs.clear();
    }
}
