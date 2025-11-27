package com.comp3607.core_game;

import com.comp3607.actions.ActionTaken;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player within the game of Jeopardy
 */
public class Player {
    //private int id;
    private String name;
    private int score;
    private List<ActionTaken> actions;

    /**
     * Creates a new Player object housing all the relevant information for a player within the game
     * @param name the name of the player
     */
    public Player(String name){
        //could do a setter in game for when i call constructor
        //this.id = id; //kinda wanna do some funny stuff with like first initial plus 00 or 01 or something
        this.name = name;
        this.score = 0;
        this.actions = new ArrayList<>();
    }
    /**
     * Returns the name of the player
     * @return the name of the player
     */
    public String getName(){
        return this.name;
    }
    /**
     * Returns the score of a player
     * @return the score of the player
     */
    public int getScore(){
        return this.score;
    }
    /**
     * Awards points to a player 
     * @param points the amount of points to be awarded
     */
    public void awardPoints(int points){
        this.score += points;
    }
    /**
     * Records an action taken by a player and adds it to the log
     * @param action the action to be added
     */
    public void recordAction(ActionTaken action){
        this.actions.add(action);
    }
    /**
     * Returns the list of actions taken by a player
     * @return the List of ActionTaken objects that exists within a player's action log
     */
    public List<ActionTaken> getActions(){
        return this.actions;
    }
}
