package com.comp3607.report_gen;

import com.comp3607.logger.GameLogger;
import com.comp3607.logger.GameEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TXTReportGenerator implements ReportGenerator {

    private final String filename;

    public TXTReportGenerator(String filename) {
        this.filename = filename;
    }

    @Override
    public void generate(GameLogger logger) {
        System.out.println("Writing report to " + filename);
        List<GameEvent> events = logger.getEvents();
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("JEOPARDY PROGRAMMING GAME REPORT\n");
            writer.write("================================\n\n");

            String caseIdStr = "N/A";
            if (!events.isEmpty() && events.get(0).caseId != null) {
                caseIdStr = events.get(0).caseId;
            }
            writer.write("Case ID: " + caseIdStr + "\n\n");

            // Collect player names
            List<String> playerNames = new ArrayList<>();
            for (GameEvent e : events) {
                if (e.playerName != null && !playerNames.contains(e.playerName)) {
                    playerNames.add(e.playerName);
                }
            }
            writer.write("Players: " + String.join(", ", playerNames) + "\n\n");

            writer.write("Gameplay Summary:\n");
            writer.write("-----------------\n");

            int turn = 1;
            for (GameEvent e : events) {
                if ("Answer Question".equals(e.activity) && e.question != null) {
                    writer.write("Turn " + turn + ": " + e.playerName + " selected " 
                                 + e.question.getCategory() + " for " 
                                 + e.question.getValue() + " pts\n");
                    writer.write("Question: " + e.question.getText() + "\n");
                    writer.write("Answer: " + e.answerGiven + " — " + e.result 
                                 + " (+" + e.question.getValue() + " pts)\n");
                    writer.write("Score after turn: " + e.playerName + " = " + e.scoreAfter + "\n\n");
                    turn++;
                }
            }

            // Final scores
            writer.write("Final Scores:\n");
            for (String playerName : playerNames) {
                int finalScore = 0;
                for (GameEvent e : events) {
                    if (playerName.equals(e.playerName)) {
                        finalScore = e.scoreAfter;
                    }
                }
                writer.write(playerName + ": " + finalScore + "\n");
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
