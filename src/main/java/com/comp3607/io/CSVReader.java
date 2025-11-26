package com.comp3607.io;

import com.comp3607.questions.Question;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.io.FileReader;

public class CSVReader implements Reader{
    private String filename;

    public CSVReader(String filename){
        this.filename = filename;
    }

    @Override
    public List<Question> read() {
        List<Question> questions = new ArrayList<>();
        try (BufferedReader input = new BufferedReader(new FileReader(filename))){
            String line = input.readLine(); //doing this to skip the column labels (Category, text, etc)
            while ((line = input.readLine()) != null){ //should be every valid line after the labels
                String[] attributes = line.split(",", -1); //a limit less than 0 keeps empty fields so the full array length can be preserved, mostly for consistency sake and actual value assignment
                String category = attributes[0];
                int value = Integer.parseInt(attributes[1]); //turn the string value to the int value
                String text = attributes[2];
                Map<Character, String> options = new HashMap<>(); //can change to linkedhashmap if the outputs are weird - reinforces order
                options.put('A', attributes[3]); //maybe also just assign this at the start for all questions as a baseline
                options.put('B', attributes[4]);
                options.put('C', attributes[5]);
                options.put('D', attributes[6]);
                char answer = attributes[7].charAt(0);
                questions.add(new Question(category, value, text, options, answer));
            }
        } catch (Exception e){
            e.printStackTrace(); //not sure how this works fully, legit just autofilled this
        }
        return questions;
    }
}
