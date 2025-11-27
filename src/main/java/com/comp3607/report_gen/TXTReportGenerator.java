package com.comp3607.report_gen;

import com.comp3607.logger.GameLogger;
import com.comp3607.logger.GameEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Generates a TXT report from the game log
 */
public class TXTReportGenerator implements ReportGenerator {

    private final String filename;
    /**
     * Creates a TXTReportGenerator with the specified filename
     * @param filename the name of the TXT file to generate
     */
    public TXTReportGenerator(String filename) {
        this.filename = filename;
    }

    /**
     * Generates the TXT report from the provided GameLogger
     * @param logger the GameLogger containing the game events to log
     */
    @Override
    public void generate(GameLogger logger) {
        System.out.println("Writing report to " + filename);
        List<GameEvent> events = logger.getEvents();
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("JEOPARDY PROGRAMMING GAME REPORT\n");
            writer.write("================================\n\n");

            // Case ID
            String caseIdStr = "N/A";
            if (!events.isEmpty()) {
                GameEvent firstEvent = events.get(0);
                if (firstEvent.getCaseId() != null) {
                    caseIdStr = firstEvent.getCaseId();
                }
            }
            writer.write("Case ID: " + caseIdStr + "\n\n");

            // Collect player names
            List<String> playerNames = new ArrayList<>();
            for (GameEvent e : events) {
                String playerName = e.getPlayerName();
                if (playerName != null && !playerNames.contains(playerName)) {
                    playerNames.add(playerName);
                }
            }
            writer.write("Players: ");
            for (int i = 0; i < playerNames.size(); i++) {
                writer.write(playerNames.get(i));
                if (i < playerNames.size() - 1) {
                    writer.write(", ");
                }
            }
            writer.write("\n\n");

            // Gameplay Summary
            writer.write("Gameplay Summary:\n");
            writer.write("-----------------\n");

            int turn = 1;
            for (GameEvent e : events) {
                if ("Answer Question".equals(e.getActivity()) && e.getQuestion() != null) {

                    String category = "";
                    if (e.getQuestion().getCategory() != null) {
                        category = e.getQuestion().getCategory();
                    }

                    int value = e.getQuestion().getValue();

                    String questionText = "";
                    if (e.getQuestion().getText() != null) {
                        questionText = e.getQuestion().getText();
                    }

                    String answerGiven = "";
                    if (e.getAnswerGiven() != null) {
                        answerGiven = e.getAnswerGiven();
                    }

                    String result = "";
                    if (e.getResult() != null) {
                        result = e.getResult();
                    }

                    writer.write("Turn " + turn + ": " + e.getPlayerName() + " selected " 
                                 + category + " for " + value + " pts\n");
                    writer.write("Question: " + questionText + "\n");

                    // Only show points if correct
                    if ("Correct".equalsIgnoreCase(result)) {
                        writer.write("Answer: " + answerGiven + " — " + result + " (+" + value + " pts)\n");
                    } else {
                        writer.write("Answer: " + answerGiven + " — " + result + "\n");
                    }

                    writer.write("Score after turn: " + e.getPlayerName() + " = " + e.getScoreAfter() + "\n\n");
                    turn++;
                }
            }

            // Final Scores
            writer.write("Final Scores:\n");
            for (int i = 0; i < playerNames.size(); i++) {
                String playerName = playerNames.get(i);
                int finalScore = 0;
                for (GameEvent e : events) {
                    if (playerName.equals(e.getPlayerName())) {
                        finalScore = e.getScoreAfter();
                    }
                }
                writer.write(playerName + ": " + finalScore + "\n");
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
