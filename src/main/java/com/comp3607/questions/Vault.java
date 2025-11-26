package com.comp3607.questions;

import java.util.ArrayList;
import java.util.List;
import com.comp3607.io.Reader;

public class Vault {
    private List<Question> questions;

    public Vault(){
        this.questions = new ArrayList<>();
    }

    public void loadQuestions(Reader reader){
        List<Question> loadedQuestions = reader.read();
        questions.addAll(loadedQuestions);
    }

    public void addQuestion(Question q){
        questions.add(q); //for testing, can add one to test vault functonality
    }
    public void removeQuestion(Question q){
        questions.remove(q); //same as above
    }

    public Question getQuestion(String category, int value){
        List<Question> all = this.getAll();
        for (Question q:all){
            if (q.getCategory().equals(category) && q.getValue()==value){
                return q;
            }
        }
        return null;  //question not found, but realistically won't happen
    }

    public List<Question> getAll(){
        return this.questions;
    }
    public void displayAll(){
        System.out.println("Jeopardy: ");
        System.out.println("-----------------------------------------------");
        for (Question q:questions){
            System.out.println(q.getCategory() + " for " + q.getValue() + " points.");
        }
        System.out.println("-----------------------------------------------");
    }
}
