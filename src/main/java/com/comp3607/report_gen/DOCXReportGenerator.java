package com.comp3607.report_gen;

import com.comp3607.logger.GameLogger;
import com.comp3607.logger.GameEvent;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DOCXReportGenerator implements ReportGenerator {

    private final String filename;

    public DOCXReportGenerator(String filename) {
        this.filename = filename;
    }

    @Override
    public void generate(GameLogger logger) {
        System.out.println("Writing DOCX report to " + filename);
        List<GameEvent> events = logger.getEvents();

        try (XWPFDocument doc = new XWPFDocument(); FileOutputStream out = new FileOutputStream(filename)) {

            // Title
            XWPFParagraph p = doc.createParagraph();
            p.createRun().setText("JEOPARDY PROGRAMMING GAME REPORT");
            doc.createParagraph().createRun().setText("================================\n");

            // Case ID
            String caseIdStr = "N/A";
            if (!events.isEmpty()) {
                GameEvent firstEvent = events.get(0);
                if (firstEvent.getCaseId() != null) {
                    caseIdStr = firstEvent.getCaseId();
                }
            }
            doc.createParagraph().createRun().setText("Case ID: " + caseIdStr + "\n");

            // Players
            List<String> playerNames = new ArrayList<>();
            for (GameEvent e : events) {
                String playerName = e.getPlayerName();
                if (playerName != null && !playerNames.contains(playerName)) {
                    playerNames.add(playerName);
                }
            }
            doc.createParagraph().createRun().setText("Players: " + String.join(", ", playerNames) + "\n");

            // Gameplay Summary
            doc.createParagraph().createRun().setText("Gameplay Summary:\n-----------------");

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

                    doc.createParagraph().createRun().setText(
                            "Turn " + turn + ": " + e.getPlayerName() + " selected " + category + " for " + value + " pts"
                    );
                    doc.createParagraph().createRun().setText("Question: " + questionText);

                    // Only show points if the answer was correct
                    if ("Correct".equalsIgnoreCase(result)) {
                        doc.createParagraph().createRun().setText(
                                "Answer: " + answerGiven + " — " + result + " (+" + value + " pts)"
                        );
                    } else {
                        doc.createParagraph().createRun().setText(
                                "Answer: " + answerGiven + " — " + result
                        );
                    }

                    doc.createParagraph().createRun().setText(
                            "Score after turn: " + e.getPlayerName() + " = " + e.getScoreAfter() + "\n"
                    );

                    turn++;
                }
            }

            // Final Scores
            doc.createParagraph().createRun().setText("Final Scores:");
            for (int i = 0; i < playerNames.size(); i++) {
                String playerName = playerNames.get(i);
                int finalScore = 0;
                for (GameEvent e : events) {
                    if (playerName.equals(e.getPlayerName())) {
                        finalScore = e.getScoreAfter();
                    }
                }
                doc.createParagraph().createRun().setText(playerName + ": " + finalScore);
            }

            doc.write(out);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
