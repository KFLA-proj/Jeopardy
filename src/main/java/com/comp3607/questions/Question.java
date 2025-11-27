package com.comp3607.questions;

import java.util.Map;

/**
 * Represents a question in the game with its category, value, text, options, and correct answer
 */
public class Question {
    private String category;
    private int value;
    private String text;
    private Map<Character, String> options; //forgot to add this earlier
    private char answer;

    /**
     * Creates a Question object which contains the question category, value, text, potential choices and correct answer
     * @param category the category of the question
     * @param value the value of the question
     * @param text the wording of the question
     * @param choices the potential choices for the question
     * @param answer the correct answer of the choices
     */
    public Question(String category, int value, String text, Map<Character,String> choices, char answer) {
        this.category = category;
        this.value = value;
        this.text = text;
        this.options = choices;
        this.answer = answer;
    }
    /**
     * Returns the category of the specified question
     * @return the category of the question as a string
     */
    public String getCategory(){
        return this.category;
    }
    /**
     * Returns the value of the specified question
     * @return the value of the question as an int
     */
    public int getValue(){
        return this.value;
    }
    /**
     * Returns the actual phrasing of the question
     * @return the text of the question as a string
     */
    public String getText(){
        return this.text;
    }
    /**
     * Return a Map of the options for the specified question
     * @return the possible options for the question
     */
    public Map<Character, String> getOptions(){
        return this.options;
    }
    /**
     * Returns the correct answer of the question
     * @return the char associated with the correct option
     */
    public char getAnswer(){
        return this.answer;
    }
    /**
     * Displays the options of the question to the console
     */
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
