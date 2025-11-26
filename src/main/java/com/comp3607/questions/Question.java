package com.comp3607.questions;

import java.util.Map;

public class Question {
    private String category;
    private int value;
    private String text;
    private Map<Character, String> options; //forgot to add this earlier
    private char answer;

    public Question(String category, int value, String text, Map<Character,String> choices, char answer) {
        this.category = category;
        this.value = value;
        this.text = text;
        this.options = choices;
        this.answer = answer;
    }

    public String getCategory(){
        return this.category;
    }
    public int getValue(){
        return this.value;
    }
    public String getText(){
        return this.text;
    }
    public Map<Character, String> getOptions(){
        return this.options;
    }
    public char getAnswer(){
        return this.answer;
    }
    public void displayOptions(){
        for (Map.Entry<Character, String> entry: this.options.entrySet()){
            System.out.println("Option: " + entry.getKey() + ": " + entry.getValue());
        }
    }

    @Override
    public String toString(){
        //gotta override this for the logging, coulda also just said getQuestion instead of overriding but oh well
        return "Category: " + category + ", Value: " + value + ", Text: " + text;
    }
}
