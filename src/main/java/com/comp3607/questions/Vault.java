package com.comp3607.questions;

import java.util.ArrayList;
import java.util.List;
import com.comp3607.io.Reader;

/**
 * Represents a vault that stores and manages a collection of questions for the game
 */
public class Vault {
    private List<Question> questions;

    /**
     * Creates an empty Vault to store questions
     */
    public Vault(){
        this.questions = new ArrayList<>();
    }
    
    /**
     * Loads questions from a Reader and adds them to the vault
     * @param reader the Reader object used to read questions depending on which strategy is chosen
     */
    public void loadQuestions(Reader reader){
        List<Question> loadedQuestions = reader.read();
        questions.addAll(loadedQuestions);
    }
    
    /**
     * Adds a question to the vault
     * @param q the Question object to be added
     */
    public void addQuestion(Question q){
        questions.add(q); //for testing, can add one to test vault functonality
    }
    /**
     * Removes a question from the vault
     * @param q the Question object to be removed
     */
    public void removeQuestion(Question q){
        questions.remove(q); //same as above
    }
    /**
     * Retrieves a question from the vault whose category and value match the ones given
     * @param category the category of the desired question
     * @param value the value of the desired question
     * @return the Question object matching the specified category and value, or null if not found
     */
    public Question getQuestion(String category, int value){
        List<Question> all = this.getAll();
        for (Question q:all){
            if (q.getCategory().equals(category) && q.getValue()==value){
                return q;
            }
        }
        return null;  //question not found, but realistically won't happen
    }
    /**
     * Retrieves all questions stored in the vault
     * @return A list of all Question objects in the vault
     */
    public List<Question> getAll(){
        return this.questions;
    }
    /**
     * Displays all questions in the vault to the console
     */
    public void displayAll(){
        System.out.println("Jeopardy: ");
        System.out.println("-----------------------------------------------");
        for (Question q:questions){
            System.out.println(q.getCategory() + " for " + q.getValue() + " points.");
        }
        System.out.println("-----------------------------------------------");
    }
}
