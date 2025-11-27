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

            // Case ID
            String caseIdStr = "N/A";
            if (!events.isEmpty()) {
                GameEvent firstEvent = events.get(0);
                if (firstEvent.getCaseId() != null) {
                    caseIdStr = firstEvent.getCaseId();
                }
            }
            document.add(new Paragraph("Case ID: " + caseIdStr + "\n"));

            // Player names
            List<String> playerNames = new ArrayList<>();
            for (GameEvent e : events) {
                String playerName = e.getPlayerName();
                if (playerName != null && !playerNames.contains(playerName)) {
                    playerNames.add(playerName);
                }
            }
            document.add(new Paragraph("Players: " + String.join(", ", playerNames) + "\n"));

            // Gameplay Summary
            document.add(new Paragraph("Gameplay Summary:\n-----------------"));
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

                    document.add(new Paragraph("Turn " + turn + ": " + e.getPlayerName() + " selected " + category + " for " + value + " pts"));
                    document.add(new Paragraph("Question: " + questionText));

                    // Only show points if the answer was correct
                    if ("Correct".equalsIgnoreCase(result)) {
                        document.add(new Paragraph("Answer: " + answerGiven + " — " + result + " (+" + value + " pts)"));
                    } else {
                        document.add(new Paragraph("Answer: " + answerGiven + " — " + result));
                    }

                    document.add(new Paragraph("Score after turn: " + e.getPlayerName() + " = " + e.getScoreAfter() + "\n"));
                    turn++;
                }
            }

            // Final Scores
            document.add(new Paragraph("Final Scores:"));
            for (int i = 0; i < playerNames.size(); i++) {
                String playerName = playerNames.get(i);
                int finalScore = 0;
                for (GameEvent e : events) {
                    if (playerName.equals(e.getPlayerName())) {
                        finalScore = e.getScoreAfter();
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
