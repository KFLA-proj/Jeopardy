package com.comp3607.runner;

import com.comp3607.core_game.*;
import com.comp3607.questions.*;
import com.comp3607.io.*;
import com.comp3607.logger.*;
//import com.comp3607.report_gen.*;
import com.comp3607.actions.*;
import java.util.Scanner;

public class JeopardySession {
    public static void main(String[] args) {
        // setup game, make scanner and stuff
        Game game = new Game();
        GameLogger logger = new GameLogger();
        Scanner scanner = new Scanner(System.in);
        game.attach(logger);

        //technically it can be empty so i wanna put the vault here and move the loadvault into each if statement so it can only load if there's a file with correct extension
        Vault vault = game.getVault();

        // 2. Choose a parser (Strategy pattern)
        System.out.print("Enter Vault FileName: "); //gotta fix this, instead of splitting backwards, just split at . instead - OUTSTANDING
        String filename = scanner.nextLine();
        int dotIndex = filename.lastIndexOf('.');
        String fileExt = ""; //to use outside the if statement incase the . is in a wonky spot
        if (dotIndex >=0 && dotIndex<filename.length()-1){
            fileExt = filename.substring(dotIndex+1).toLowerCase();
        }
        if (fileExt.equals("csv")){
            Reader reader = new CSVReader(filename);
            System.out.println("opened csv");
            vault.loadQuestions(reader);
        } else if (fileExt.equals("xml")){ // broken
            Reader reader = new XMLReader(filename);
            System.out.println("opened xml");
            vault.loadQuestions(reader);
        } else if (fileExt.equals("json")){
            Reader reader = new JSONReader(filename);
            System.out.println("opened json");
            vault.loadQuestions(reader);
        } else { //redundant? yes. works? also yes.
            System.out.println("Unexpected File Type.");
            System.exit(0); //i guess?
        }
        
        // 4. Create players
        System.out.print("Enter number of players: ");
        int numPlayers = (scanner.nextInt());
        scanner.nextLine(); // newline char messing with my input
        for (int x=0; x<numPlayers; x++){
            System.out.print("Enter player " + (x+1) + " name: ");
            String name = scanner.nextLine();
            Player p = new Player(name);
            game.getPlayers().add(p);
        }

        game.getVault().displayAll();
        int x=0;
        while (true){
            if (!game.hasActiveQuestion()){
                System.out.println("[" + game.getPlayers().get(x).getName() + "] Choose a question by category and value: (Eg: File Handling 200)"); // just wanna add some player tracking cause its kinda easy to lose track rn
                String string = scanner.nextLine();
                if (string.equals("exit")){break;}
                String cat = string.substring(0, string.length()-3).trim(); //THE PROBLEM WAS WHITESPACE AFTER TRUNCATE cause i split at the start of value so there was perma whitespace
                int val = Integer.valueOf(string.substring(string.length()-3, string.length()));
                Question q = vault.getQuestion(cat, val);
                if (q==null){
                    System.out.println("No Question found for " + cat + " at " + val); //somehow i managed to write everything blind with no validation - my genius is truly frightening or atleast it would be if it worked
                }
                ActionTaken selectedQuestion = new SelectQuestionAction(game.getPlayers().get(x), q);
                selectedQuestion.execute(game);
                //vault.getQuestion(cat, val).displayOptions(); //put it in execute instead
                System.out.println("Choose your answer: (A,B,C,D)");
                String answer = scanner.nextLine();
                if (answer.equals("exit")){break;}
                char ans = Character.toUpperCase(answer.charAt(0));
                ActionTaken answeredQuestion = new AnswerQuestionAction(game.getPlayers().get(x), q, ans);
                answeredQuestion.execute(game);
                if (game.hasActiveQuestion()){
                    if (x==(game.getPlayers().size())-1){x=0;}
                    else {x++;}
                }
            }else if (game.hasActiveQuestion()){
                Question q = game.getActiveQuestion();
                q.displayOptions();
                System.out.println("Choose your answer: (A,B,C,D)");
                String answer = scanner.nextLine();
                if (answer.equals("exit")){break;}
                char ans = Character.toUpperCase(answer.charAt(0));
                ActionTaken answeredQuestion = new AnswerQuestionAction(game.getPlayers().get(x), q, ans);
                answeredQuestion.execute(game);
                if (game.hasActiveQuestion()){
                    if (x==(game.getPlayers().size())-1){x=0;}
                    else {x++;}
                }
            }
        }

        // 7. Print final scores
        System.out.println("\nFinal Scores:");
        for (Player p : game.getPlayers()) {
            System.out.println(p.getName() + ": " + p.getScore());
        }
        scanner.close();
    }
}