package com.comp3607.runner;

import com.comp3607.core_game.*;
import com.comp3607.questions.*;
import com.comp3607.io.*;
import com.comp3607.logger.*;
import com.comp3607.actions.*;
import com.comp3607.report_gen.*;

import java.util.Scanner;
import java.io.File;

public class JeopardySession {
    public static void main(String[] args) {
        Game game = new Game();
        GameLogger logger = new GameLogger("GAME001"); // For report generation
        game.attach(logger);

        Scanner scanner = new Scanner(System.in);
        Vault vault = game.getVault();

        // --- File Selection ---
        Reader reader = null;
        while (reader == null) {
            System.out.println("Select the game file to load:");
            System.out.println("1. CSV");
            System.out.println("2. JSON");
            System.out.println("3. XML");
            System.out.print("Enter choice (1-3): ");
            String choice = scanner.nextLine().trim();

            if (choice.equals("1")) reader = new CSVReader("sample_game_CSV.csv");
            else if (choice.equals("2")) reader = new JSONReader("sample_game_JSON.json");
            else if (choice.equals("3")) reader = new XMLReader("sample_game_XML.xml");
            else {
                System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                continue;
            }
            System.out.println("Loaded game file.\n");
        }

        vault.loadQuestions(reader);

        // --- Number of Players ---
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
                System.out.println("Invalid input. Enter a number between 1 and 4.");
                scanner.nextLine();
            }
        }

        for (int i = 0; i < numPlayers; i++) {
            System.out.print("Enter player " + (i + 1) + " name: ");
            String name = scanner.nextLine().trim();
            game.getPlayers().add(new Player(name));
        }

        // --- Display all questions at the start ---
        System.out.println("\nJeopardy Questions Available:");
        System.out.println("-----------------------------------------------");
        vault.displayAll();
        System.out.println("-----------------------------------------------");

        int currentPlayerIndex = 0;

        // --- Main Game Loop ---
        while (vault.getAll().size() > 0) {
            Player currentPlayer = game.getPlayers().get(currentPlayerIndex);

            // --- Select a question ---
            Question selectedQuestion = null;
            while (selectedQuestion == null) {
                System.out.println("[" + currentPlayer.getName() + "] Choose a question (or type 'exit' to quit):");
                String input = scanner.nextLine().trim();
                if (input.equalsIgnoreCase("exit")) {
                    System.out.println("Game exited by player.");
                    vault.getAll().clear(); // Force end game
                    break;
                }

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

            if (selectedQuestion == null) break; // Exit game

            // --- Show question ---
            System.out.println("\nQuestion: " + selectedQuestion.getText());
            selectedQuestion.displayOptions();

            // --- Answer the question ---
            char answer = ' ';
            while (true) {
                System.out.println("[" + currentPlayer.getName() + "] Choose your answer (A, B, C, D) or type 'exit':");
                String ansInput = scanner.nextLine().trim();
                if (ansInput.equalsIgnoreCase("exit")) {
                    System.out.println("Game exited by player.");
                    vault.getAll().clear(); // Force end game
                    break;
                }
                if (ansInput.length() == 1) {
                    answer = Character.toUpperCase(ansInput.charAt(0));
                    if (answer >= 'A' && answer <= 'D') break;
                }
                System.out.println("Invalid input. Please enter A, B, C, or D.");
            }

            if (vault.getAll().isEmpty()) break; // Exit if game ended

            // --- Process answer ---
            ActionTaken answerAction = new AnswerQuestionAction(currentPlayer, selectedQuestion, answer);
            answerAction.execute(game);

            // Compare player's answer to the correct answer
            boolean correct = (answer == selectedQuestion.getAnswer());
            int points = correct ? selectedQuestion.getValue() : 0;

            System.out.println("[" + currentPlayer.getName() + "] " + (correct ? "Correct!" : "Incorrect!") +
                    " +" + points + " points" +
                    " (Total: " + currentPlayer.getScore() + ")");

            // --- Remove question from vault ---
            vault.removeQuestion(selectedQuestion);

            // --- Show remaining questions ---
            System.out.println("\nRemaining Questions:");
            System.out.println("-----------------------------------------------");
            vault.displayAll();
            System.out.println("-----------------------------------------------");

            // --- Next player ---
            currentPlayerIndex = (currentPlayerIndex + 1) % game.getPlayers().size();
        }

        // --- Final Scores ---
        System.out.println("\nFinal Scores:");
        for (Player p : game.getPlayers()) {
            System.out.println(p.getName() + ": " + p.getScore());
        }

        // --- Generate reports ---
        try {
            String outputFolder = "reports";
            new File(outputFolder).mkdirs();

            TXTReportGenerator txtReport = new TXTReportGenerator(outputFolder + "/game_report.txt");
            CSVReportGenerator csvReport = new CSVReportGenerator(outputFolder + "/game_event_log.csv");
            DOCXReportGenerator docxReport = new DOCXReportGenerator(outputFolder + "/game_report.docx");
            PDFReportGenerator pdfReport = new PDFReportGenerator(outputFolder + "/game_report.pdf");

            txtReport.generate(logger);
            csvReport.generate(logger);
            docxReport.generate(logger);
            pdfReport.generate(logger);

            System.out.println("\nAll reports generated in: " + outputFolder);
        } 
        catch (Exception e) {
            System.out.println("Error generating reports: " + e.getMessage());
        }

        scanner.close();
    }
}
