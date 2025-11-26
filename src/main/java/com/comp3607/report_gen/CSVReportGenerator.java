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
                if (e.caseId != null) {
                    caseIdStr = e.caseId;
                }

                String playerNameStr = "";
                if (e.playerName != null) {
                    playerNameStr = e.playerName;
                }

                String activityStr = "";
                if (e.activity != null) {
                    activityStr = e.activity;
                }

                String timestampStr = "";
                if (e.timestamp != null) {
                    timestampStr = e.timestamp.format(FORMATTER);
                }

                String categoryStr = "";
                if (e.category != null) {
                    categoryStr = e.category;
                }

                String answerGivenStr = escapeCSV(e.answerGiven);
                String resultStr = escapeCSV(e.result);
                String questionValueStr = String.valueOf(e.questionValue);
                String scoreAfterStr = String.valueOf(e.scoreAfter);

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

        } catch (IOException ex) {
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
