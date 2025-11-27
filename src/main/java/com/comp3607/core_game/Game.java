package com.comp3607.core_game;

import java.util.ArrayList;
import java.util.List;

import com.comp3607.logger.GameLogger;
import com.comp3607.questions.*;
//not sure yet if to import player, observer and this next thing

/**
 * Represents an instance of the game of Jeopardy
 */
public class Game {
    private List<Player> players;
    private Vault vault;
    private List<GameObserver> observers;
    private Question activeQuestion;//not quite sure if i wanna use this yet, card for removal

    /**
     * Creates a new Game object which houses all the relevant information for the game
     */
    public Game(){
        this.players = new ArrayList<>();
        this.vault = new Vault();
        this.observers = new ArrayList<>();
        this.activeQuestion = null;
    }

    //observer methods - might need to edit notify, i can never remember how it works
    /**
     * Attaches an observer to the list of observers for this game object
     * @param obs the observer being added to the list
     */
    public void attach(GameObserver obs){
        this.observers.add(obs);
    }
    /**
     * Detaches an observer from the list of observers for this game object
     * @param obs the observer being detached from the list
     */
    public void detach(GameObserver obs){
        this.observers.remove(obs);
    }
    /**
     * Notifies all observers of a particular message
     * @param message the message being delivered
     */
    public void notify(String message){
        for (GameObserver obs : observers){
            obs.update(message);
        }
    }

    /**
     * Returns the list of players playing the game
     * @return a List object containing the players currently playing the game
     */
    public List<Player> getPlayers(){
        return this.players;
    }
    /**
     * Returns the question vault for the game instance
     * @return a Vault object containing all of the question data for the game
     */
    public Vault getVault(){
        return this.vault;
    }
    /**
     * Sets a question as active in the game 
     * @param q the question to be set as active
     */
    public void setActiveQuestion(Question q){
        this.activeQuestion = q;
    }
    /**
     * Removes the currently active question in the game object
     */
    public void dropActiveQuestion(){
        this.activeQuestion = null;
    }
    /**
     * Checks whether or not the game has an active question set
     * @return boolean stating whether or not the game object currently has an active question
     */
    public boolean hasActiveQuestion(){
        return this.activeQuestion!=null; //trying to do the turn passing thing so if one person doesn't get it then it can just pass along to next to answer
    }
    /**
     * Returns all observers currently attached to the game object
     * @return the List of GameObserver objects currently attached to the game object
     */
    public List<GameObserver> getObservers() {
        return this.observers;
    }
    /**
     * Returns the Question object currently set as active in the game object
     * @return Question currently set as active
     */
    public Question getActiveQuestion(){
        return this.activeQuestion;
    }
    /**
     * Ends the current game instance and sends requisite message through attached observers
     */
    public void endGame() {
        notify("Game Over!");

        for (GameObserver obs : observers) {
            if (obs instanceof GameLogger logger) {
                logger.generateReports();
            }
        }
    }

}
