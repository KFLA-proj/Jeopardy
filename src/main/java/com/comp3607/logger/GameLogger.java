package com.comp3607.logger;

import com.comp3607.core_game.GameObserver;
import com.comp3607.core_game.Player;
import com.comp3607.questions.Question;
import com.comp3607.report_gen.CSVReportGenerator;
import com.comp3607.report_gen.DOCXReportGenerator;
import com.comp3607.report_gen.PDFReportGenerator;
import com.comp3607.report_gen.TXTReportGenerator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class GameLogger implements GameObserver {
    private static int gameCounter = 1; // auto-incrementing game ID
    private final List<String> log;
    private final List<GameEvent> events;
    private final String caseId;

    // Constructor with automatic GAME ID
    public GameLogger() {
        this.caseId = String.format("GAME%03d", gameCounter++);
        this.log = new ArrayList<>();
        this.events = new ArrayList<>();
        log.add(caseId + ",System,Start Game," + now() + ",,,," );
    }

    // Constructor with manual GAME ID
    public GameLogger(String caseId) {
        this.caseId = caseId;
        this.log = new ArrayList<>();
        this.events = new ArrayList<>();
        log.add(caseId + ",System,Start Game," + now() + ",,,," );
    }

    @Override
    public void update(String message) {
        log.add("[" + now() + "] " + message);
        System.out.println("SYS: " + message);
    }

    private String now() {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public void logTurn(Player player, String activity, Question question, String answerGiven, String result) {
        int score = player.getScore();

        // Create GameEvent using new constructor
        GameEvent event = new GameEvent(
            this.caseId,
            player.getName(),
            activity,
            LocalDateTime.now(),
            question,          // pass Question object directly
            answerGiven != null ? answerGiven : "",
            result != null ? result : "",
            score
        );

        events.add(event);

        // Log string for CSV/text
        log.add(String.join(",",
            event.getCaseId(),
            event.getPlayerName(),
            event.getActivity(),
            event.getTimestamp().toString(),
            event.getCategory() != null ? event.getCategory() : "",
            String.valueOf(event.getQuestionValue()),
            event.getAnswerGiven(),
            event.getResult(),
            String.valueOf(event.getScoreAfter())
        ));
    }

    public List<String> getLogs() {
        return new ArrayList<>(log);
    }

    public List<GameEvent> getEvents() {
        return new ArrayList<>(events);
    }

    public String getCaseId() {
        return caseId;
    }

    public void generateReports() {
        // TXT report
        try {
            TXTReportGenerator txtGen = new TXTReportGenerator(caseId + "_report.txt");
            txtGen.generate(this);
        } catch (Exception e) {
            System.err.println("Failed to generate TXT report: " + e.getMessage());
            e.printStackTrace();
        }

        // CSV report
        try {
            CSVReportGenerator csvGen = new CSVReportGenerator(caseId + "_report.csv");
            csvGen.generate(this);
        } catch (Exception e) {
            System.err.println("Failed to generate CSV report: " + e.getMessage());
            e.printStackTrace();
        }

        // DOCX report
        try {
            DOCXReportGenerator docxGen = new DOCXReportGenerator(caseId + "_report.docx");
            docxGen.generate(this);
        } catch (Exception e) {
            System.err.println("Failed to generate DOCX report: " + e.getMessage());
            e.printStackTrace();
        }

        // PDF report
        try {
            PDFReportGenerator pdfGen = new PDFReportGenerator(caseId + "_report.pdf");
            pdfGen.generate(this);
        } catch (Exception e) {
            System.err.println("Failed to generate PDF report: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("Reports generated (attempted): " 
            + caseId + "_report.txt, " 
            + caseId + "_report.csv, " 
            + caseId + "_report.docx, " 
            + caseId + "_report.pdf");
    }

}
