package com.comp3607.io;

import com.comp3607.questions.Question;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
//tryna learn jackson - https://www.geeksforgeeks.org/java/how-to-read-and-write-json-files-in-java if you wanna learn it reems
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;

public class JSONReader implements Reader{
    private String filename;

    public JSONReader(String filename) {
        this.filename = filename;
    }


    public List<Question> read() {
        List<Question> questions = new ArrayList<>();
        try {
            ObjectMapper om = new ObjectMapper();
            JsonNode jn = om.readTree(new File(filename));
            for (JsonNode x:jn){ //this is supposed to be every node in the "map", basically the entire file
                String category = x.get("Category").asText();
                int value = x.get("Value").asInt();
                String text = x.get("Question").asText();
                Map<Character, String> options = new HashMap<>();
                JsonNode choices = x.get("Options"); //since its like a subdirectory - gotta map again and iterate separately from main
                options.put('A', choices.get("A").asText()); //kinda like putting the key and then appending the text on the key to itself? (<--main breakpoint, confirm if works)
                options.put('B', choices.get("B").asText());
                options.put('C', choices.get("C").asText());
                options.put('D', choices.get("D").asText());
                String temp = x.get("CorrectAnswer").asText(); // double check char vs Character, i'm very inconsistent with it and i'm now noticing
                char answer = temp.charAt(0); //ITS JANK IK BUT IT SHOULD WORK
                questions.add(new Question(category, value, text, options, answer));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return questions;
    }
}
