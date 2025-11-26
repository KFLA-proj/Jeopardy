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

            XWPFParagraph p = doc.createParagraph();
            p.createRun().setText("JEOPARDY PROGRAMMING GAME REPORT");
            doc.createParagraph().createRun().setText("================================\n");

            String caseIdStr = "N/A";
            if (!events.isEmpty() && events.get(0).caseId != null) {
                caseIdStr = events.get(0).caseId;
            }
            doc.createParagraph().createRun().setText("Case ID: " + caseIdStr + "\n");

            // Players
            List<String> playerNames = new ArrayList<>();
            for (GameEvent e : events) {
                if (e.playerName != null && !playerNames.contains(e.playerName)) {
                    playerNames.add(e.playerName);
                }
            }
            doc.createParagraph().createRun().setText("Players: " + String.join(", ", playerNames) + "\n");

            doc.createParagraph().createRun().setText("Gameplay Summary:\n-----------------");

            int turn = 1;
            for (GameEvent e : events) {
                if ("Answer Question".equals(e.activity) && e.question != null) {
                    doc.createParagraph().createRun().setText("Turn " + turn + ": " + e.playerName + " selected " 
                            + e.question.getCategory() + " for " 
                            + e.question.getValue() + " pts");
                    doc.createParagraph().createRun().setText("Question: " + e.question.getText());
                    doc.createParagraph().createRun().setText("Answer: " + e.answerGiven + " — " + e.result 
                            + " (+" + e.question.getValue() + " pts)");
                    doc.createParagraph().createRun().setText("Score after turn: " + e.playerName + " = " + e.scoreAfter + "\n");
                    turn++;
                }
            }

            doc.createParagraph().createRun().setText("Final Scores:");
            for (String playerName : playerNames) {
                int finalScore = 0;
                for (GameEvent e : events) {
                    if (playerName.equals(e.playerName)) {
                        finalScore = e.scoreAfter;
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
