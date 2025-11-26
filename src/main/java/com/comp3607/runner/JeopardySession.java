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

        vault.displayAll();

        int currentPlayerIndex = 0;

        // Game
        while (vault.getAll().size() > 0) { 
            Player currentPlayer = game.getPlayers().get(currentPlayerIndex);

            Question selectedQuestion = null;
            while (selectedQuestion == null) {
                System.out.println("[" + currentPlayer.getName() + "] Choose a question by category and value (e.g., File Handling 200):");
                String input = scanner.nextLine().trim();
                try {
                    int spaceIndex = input.lastIndexOf(' ');
                    String category = input.substring(0, spaceIndex).trim();
                    int value = Integer.parseInt(input.substring(spaceIndex + 1));

                    selectedQuestion = vault.getQuestion(category, value);
                    if (selectedQuestion == null) {
                        System.out.println("No question found for " + category + " at " + value);
                    }

                } catch (Exception e) {
                    System.out.println("Invalid input format. Use 'Category Value' e.g., File Handling 200");
                }
            }

            System.out.println("\nQuestion: " + selectedQuestion.getText());
            selectedQuestion.displayOptions();

            char answer = ' ';
            while (true) {
                System.out.println("[" + currentPlayer.getName() + "] Choose your answer: (A, B, C, D)");
                String ansInput = scanner.nextLine().trim();
                if (ansInput.length() == 1) {
                    answer = Character.toUpperCase(ansInput.charAt(0));
                    if (answer >= 'A' && answer <= 'D') break;
                }
                System.out.println("Invalid input. Please enter A, B, C, or D.");
            }

            ActionTaken answerAction = new AnswerQuestionAction(currentPlayer, selectedQuestion, answer);
            answerAction.execute(game);

            vault.removeQuestion(selectedQuestion);
            vault.displayAll();

            currentPlayerIndex = (currentPlayerIndex + 1) % game.getPlayers().size();
        }

        System.out.println("\nFinal Scores:");
        for (Player p : game.getPlayers()) {
            System.out.println(p.getName() + ": " + p.getScore());
        }

        scanner.close();
    }
}
