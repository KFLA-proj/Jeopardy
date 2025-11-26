package com.comp3607.core_game;

import java.util.ArrayList;
import java.util.List;

import com.comp3607.logger.GameLogger;
import com.comp3607.questions.*;
//not sure yet if to import player, observer and this next thing

public class Game {
    private List<Player> players;
    private Vault vault;
    private List<GameObserver> observers;
    private Question activeQuestion;//not quite sure if i wanna use this yet, card for removal

    public Game(){
        this.players = new ArrayList<>();
        this.vault = new Vault();
        this.observers = new ArrayList<>();
        this.activeQuestion = null;
    }

    //observer methods - might need to edit notify, i can never remember how it works
    public void attach(GameObserver obs){
        this.observers.add(obs);
    }
    public void detach(GameObserver obs){
        this.observers.remove(obs);
    }
    public void notify(String message){
        for (GameObserver obs : observers){
            obs.update(message);
        }
    }

    public List<Player> getPlayers(){
        return this.players;
    }
    public Vault getVault(){
        return this.vault;
    }
    public void setActiveQuestion(Question q){
        this.activeQuestion = q;
    }
    public void dropActiveQuestion(){
        this.activeQuestion = null;
    }
    public boolean hasActiveQuestion(){
        return this.activeQuestion!=null; //trying to do the turn passing thing so if one person doesn't get it then it can just pass along to next to answer
    }

    public List<GameObserver> getObservers() {
        return this.observers;
    }
    public Question getActiveQuestion(){
        return this.activeQuestion;
    }

    public void endGame() {
        notify("Game Over!");

        for (GameObserver obs : observers) {
            if (obs instanceof GameLogger logger) {
                logger.generateReports();
            }
        }
    }

}
