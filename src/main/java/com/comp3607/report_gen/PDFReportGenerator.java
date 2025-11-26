package com.comp3607.report_gen;

import com.comp3607.logger.GameLogger;
import com.comp3607.logger.GameEvent;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.util.ArrayList;
import java.util.List;

public class PDFReportGenerator implements ReportGenerator {

    private final String filename;

    public PDFReportGenerator(String filename) {
        this.filename = filename;
    }

    @Override
    public void generate(GameLogger logger) {
        System.out.println("Writing PDF report to " + filename);
        List<GameEvent> events = logger.getEvents();

        try {
            PdfWriter writer = new PdfWriter(filename);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("JEOPARDY PROGRAMMING GAME REPORT"));
            document.add(new Paragraph("================================\n"));

            String caseIdStr = "N/A";
            if (!events.isEmpty() && events.get(0).caseId != null) {
                caseIdStr = events.get(0).caseId;
            }
            document.add(new Paragraph("Case ID: " + caseIdStr + "\n"));

            // Player names
            List<String> playerNames = new ArrayList<>();
            for (GameEvent e : events) {
                if (e.playerName != null && !playerNames.contains(e.playerName)) {
                    playerNames.add(e.playerName);
                }
            }
            document.add(new Paragraph("Players: " + String.join(", ", playerNames) + "\n"));

            document.add(new Paragraph("Gameplay Summary:\n-----------------"));
            int turn = 1;
            for (GameEvent e : events) {
                if ("Answer Question".equals(e.activity) && e.question != null) {
                    document.add(new Paragraph("Turn " + turn + ": " + e.playerName + " selected " 
                            + e.question.getCategory() + " for " 
                            + e.question.getValue() + " pts"));
                    document.add(new Paragraph("Question: " + e.question.getText()));
                    document.add(new Paragraph("Answer: " + e.answerGiven + " — " + e.result 
                            + " (+" + e.question.getValue() + " pts)"));
                    document.add(new Paragraph("Score after turn: " + e.playerName + " = " + e.scoreAfter + "\n"));
                    turn++;
                }
            }

            // Final scores
            document.add(new Paragraph("Final Scores:"));
            for (String playerName : playerNames) {
                int finalScore = 0;
                for (GameEvent e : events) {
                    if (playerName.equals(e.playerName)) {
                        finalScore = e.scoreAfter;
                    }
                }
                document.add(new Paragraph(playerName + ": " + finalScore));
            }

            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
