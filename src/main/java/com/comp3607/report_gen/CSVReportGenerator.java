package com.comp3607.report_gen;

import com.comp3607.logger.GameLogger;
import com.comp3607.logger.GameEvent;

import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CSVReportGenerator implements ReportGenerator {

    private final String filename;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public CSVReportGenerator(String filename) {
        this.filename = filename;
    }

    @Override
    public void generate(GameLogger logger) {
        List<GameEvent> events = logger.getEvents();
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("Case_ID,Player_ID,Activity,Timestamp,Category,Question_Value,Answer_Given,Result,Score_After_Play\n");

            for (GameEvent e : events) {
                String caseIdStr = "";
                if (e.getCaseId() != null) {
                    caseIdStr = e.getCaseId();
                }

                String playerNameStr = "";
                if (e.getPlayerName() != null) {
                    playerNameStr = e.getPlayerName();
                }

                String activityStr = "";
                if (e.getActivity() != null) {
                    activityStr = e.getActivity();
                }

                String timestampStr = "";
                if (e.getTimestamp() != null) {
                    timestampStr = e.getTimestamp().format(FORMATTER);
                }

                String categoryStr = "";
                if (e.getCategory() != null) {
                    categoryStr = e.getCategory();
                }

                String answerGivenStr = "";
                if (e.getAnswerGiven() != null) {
                    answerGivenStr = escapeCSV(e.getAnswerGiven());
                }

                String resultStr = "";
                if (e.getResult() != null) {
                    resultStr = escapeCSV(e.getResult());
                }

                String questionValueStr = String.valueOf(e.getQuestionValue());
                String scoreAfterStr = String.valueOf(e.getScoreAfter());

                writer.write(String.join(",",
                        caseIdStr,
                        playerNameStr,
                        activityStr,
                        timestampStr,
                        categoryStr,
                        questionValueStr,
                        answerGivenStr,
                        resultStr,
                        scoreAfterStr
                ));
                writer.write("\n");
            }

        } 
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private String escapeCSV(String value) {
        if (value == null || value.isEmpty()) return "";
        if (value.contains(",") || value.contains("\"")) {
            value = value.replace("\"", "\"\"");
            return "\"" + value + "\"";
        }
        return value;
    }
}
