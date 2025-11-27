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

/**
 * Logs game events and generates reports in various formats
 */
public class GameLogger implements GameObserver {
    private static int gameCounter = 1; // auto-incrementing game ID
    private final List<String> log;
    private final List<GameEvent> events;
    private final String caseId;

    // Constructor with automatic GAME ID
    /**
     * Creates a GameLogger instance with an auto-generated caseID
     */
    public GameLogger() {
        this.caseId = String.format("GAME%03d", gameCounter++);
        this.log = new ArrayList<>();
        this.events = new ArrayList<>();
        log.add(caseId + ",System,Start Game," + now() + ",,,," );
    }

    // Constructor with manual GAME ID
    /**
     * Creates a GameLogger instance with a specified caseID
     * @param caseId the caseID to assign to this game logger
     */
    public GameLogger(String caseId) {
        this.caseId = caseId;
        this.log = new ArrayList<>();
        this.events = new ArrayList<>();
        log.add(caseId + ",System,Start Game," + now() + ",,,," );
    }

    /**
     * Updates the logger with a new message
     * @param message the message to log
     */
    @Override
    public void update(String message) {
        log.add("[" + now() + "] " + message);
        System.out.println("SYS: " + message);
    }

    /**
     * Returns the current timestamp as a formatted string
     * @return the current timestamp
     */
    private String now() {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
    /**
     * Logs a player's turn with all relevant details
     * @param player the player taking the turn
     * @param activity the activity performed
     * @param question the question involved in the turn
     * @param answerGiven the answer provided by the player
     * @param result the result of the answer check
     */
    public void logTurn(Player player, String activity, Question question, String answerGiven, String result) {
        int score = player.getScore();

        // Replace nulls with empty strings
        if (answerGiven == null) {
            answerGiven = "";
        }
        if (result == null) {
            result = "";
        }

        GameEvent event = new GameEvent(
            this.caseId,
            player.getName(),
            activity,
            LocalDateTime.now(),
            question,          
            answerGiven,
            result,
            score
        );

        events.add(event);

        String category = event.getCategory();
        if (category == null) {
            category = "";
        }

        log.add(String.join(",",
            event.getCaseId(),
            event.getPlayerName(),
            event.getActivity(),
            event.getTimestamp().toString(),
            category,
            String.valueOf(event.getQuestionValue()),
            event.getAnswerGiven(),
            event.getResult(),
            String.valueOf(event.getScoreAfter())
        ));
    }

    /**
     * Returns the list of log entries
     * @return the list of log strings
     */
    public List<String> getLogs() {
        return new ArrayList<>(log);
    }

    /**
     * Returns the list of game events
     * @return the list of GameEvent objects
     */
    public List<GameEvent> getEvents() {
        return new ArrayList<>(events);
    }
    
    /**
     * Returns the caseID for this game logger
     * @return the caseID as a string
     */
    public String getCaseId() {
        return caseId;
    }
    /**
     * Generates reports in TXT, CSV, DOCX, and PDF formats
     */
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
