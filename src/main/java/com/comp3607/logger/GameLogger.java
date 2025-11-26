package com.comp3607.logger;

import com.comp3607.core_game.GameObserver;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class GameLogger implements GameObserver{
    private List<String> log;
    private LocalDateTime timestamp;

    public GameLogger(){
        this.log = new ArrayList<>();
    }

    @Override
    public void update(String message){
        this.timestamp = LocalDateTime.now();
        this.log.add("[" + timestamp.toString() + "] " + message);
        System.out.println("SYS: " + message);
    }

    public List<String> getLogs(){
        return this.log;
    }
}