package com.comp3607.runner;

import com.comp3607.core_game.*;
import com.comp3607.questions.*;
import com.comp3607.io.*;
import com.comp3607.logger.*;
import com.comp3607.actions.*;

import java.util.Scanner;

public class JeopardySession {
    public static void main(String[] args) {
        Game game = new Game();
        GameLogger logger = new GameLogger();
        game.attach(logger);
        Scanner scanner = new Scanner(System.in);
        Vault vault = game.getVault();

        // File Selection
        Reader reader = null;
        while (reader == null) {
            System.out.println("Select the game file to load:");
            System.out.println("1. CSV");
            System.out.println("2. JSON");
            System.out.println("3. XML");
            System.out.print("Enter choice (1-3): ");
            String choice = scanner.nextLine().trim();

            if (choice.equals("1")) {
                reader = new CSVReader("sample_game_CSV.csv");
                System.out.println("Loaded CSV file.");
            } else if (choice.equals("2")) {
                reader = new JSONReader("sample_game_JSON.json");
                System.out.println("Loaded JSON file.");
            } else if (choice.equals("3")) {
                reader = new XMLReader("sample_game_XML.xml");
                System.out.println("Loaded XML file.");
            } else {
                System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            }
        }

        vault.loadQuestions(reader);

        // Number of Plyaers
        int numPlayers = 0;
        while (numPlayers < 1 || numPlayers > 4) {
            System.out.print("Enter number of players (1-4): ");
            if (scanner.hasNextInt()) {
                numPlayers = scanner.nextInt();
                scanner.nextLine(); // consume newline
                if (numPlayers < 1 || numPlayers > 4) {
                    System.out.println("Invalid number of players. Must be between 1 and 4.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number between 1 and 4.");
                scanner.nextLine();
            }
        }

        for (int i = 0; i < numPlayers; i++) {
            System.out.print("Enter player " + (i + 1) + " name: ");
            String name = scanner.nextLine().trim();
            game.getPlayers().add(new Player(name));
        }

        

        int x=0;
        while (true){
            if (!game.hasActiveQuestion()){
                vault.displayAll();
                System.out.println("[" + game.getPlayers().get(x).getName() + "] Choose a question by category and value: (Eg: File Handling 200)"); // just wanna add some player tracking cause its kinda easy to lose track rn
                String string = scanner.nextLine();
                if (string.equals("exit")){
                    break;
                }
                try{
                    String cat = string.substring(0, string.length()-3).trim(); //THE PROBLEM WAS WHITESPACE AFTER TRUNCATE cause i split at the start of value so there was perma whitespace
                    int val = Integer.valueOf(string.substring(string.length()-3, string.length()));
                    Question q = vault.getQuestion(cat, val);
                    if (q==null){
                        System.out.println("No Question found for " + cat + " at " + val); //somehow i managed to write everything blind with no validation - my genius is truly frightening or atleast it would be if it worked
                    }
                    int score = q.getValue();
                    ActionTaken selectedQuestion = new SelectQuestionAction(game.getPlayers().get(x), q);
                    System.out.println(q.getText());
                    selectedQuestion.execute(game);
                    System.out.println("[" + game.getPlayers().get(x).getName() + "] Choose your answer: (A,B,C,D)");
                    String answer = scanner.nextLine();
                    if (answer.equals("exit")){break;}
                    try{
                        char ans = Character.toUpperCase(answer.charAt(0));
                        if (ans!='A' && ans!='B' && ans!='C' && ans!='D'){ //brute forcing but close to deadline so oh well @ reems if you think you can clean it up then go ahead
                            throw new Exception("Invalid Format");
                        }
                        ActionTaken answeredQuestion = new AnswerQuestionAction(game.getPlayers().get(x), q, ans);
                        answeredQuestion.execute(game);
                        if (game.hasActiveQuestion()){
                            if (x==(game.getPlayers().size())-1){x=0;}
                            else {x++;}
                            System.out.println("Incorrect Answer!");
                        }   
                    } catch (Exception e){
                        System.out.println("Invalid input format. Use Option Char, e.g., 'A'");
                        continue;
                    }
                    System.out.println("[" + game.getPlayers().get(x).getName() + "] + " + score + " points!");
                } catch (Exception e){
                    System.out.println("Invalid input format. Use 'Category Value' e.g., File Handling 200");
                    continue;
                }                
            } else if (game.hasActiveQuestion()){
                Question q = game.getActiveQuestion();
                int score = q.getValue();
                System.out.println(q.getText());
                q.displayOptions();
                System.out.println("[" + game.getPlayers().get(x).getName() + "] Choose your answer: (A,B,C,D)");
                String answer = scanner.nextLine();
                if (answer.equals("exit")){break;}
                try{
                    char ans = Character.toUpperCase(answer.charAt(0));
                    if (ans!='A' && ans!='B' && ans!='C' && ans!='D'){
                        throw new Exception("Invalid Format");
                    }
                    ActionTaken answeredQuestion = new AnswerQuestionAction(game.getPlayers().get(x), q, ans);
                    answeredQuestion.execute(game);
                    if (game.hasActiveQuestion()){
                        if (x==(game.getPlayers().size())-1){x=0;}
                        else {x++;}
                        System.out.println("Incorrect Answer!");
                    }   
                } catch (Exception e){
                    System.out.println("Invalid input format. Use Option Char, e.g., 'A'");
                    continue;
                }
                System.out.println("[" + game.getPlayers().get(x).getName() + "] scored " + score + " points!");
            }
        }

        System.out.println("\nFinal Scores:");
        for (Player p : game.getPlayers()) {
            System.out.println(p.getName() + ": " + p.getScore());
        }

        scanner.close();
    }   
}
