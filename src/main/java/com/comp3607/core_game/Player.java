package com.comp3607.core_game;

import com.comp3607.actions.ActionTaken;
import java.util.ArrayList;
import java.util.List;

public class Player {
    //private int id;
    private String name;
    private int score;
    private List<ActionTaken> actions;

    public Player(String name){
        //could do a setter in game for when i call constructor
        //this.id = id; //kinda wanna do some funny stuff with like first initial plus 00 or 01 or something
        this.name = name;
        this.score = 0;
        this.actions = new ArrayList<>();
    }

    public String getName(){
        return this.name;
    }
    public int getScore(){
        return this.score;
    }

    public void awardPoints(int points){
        this.score += points;
    }
    public void recordAction(ActionTaken action){
        this.actions.add(action);
    }

    public List<ActionTaken> getActions(){
        return this.actions;
    }
}
