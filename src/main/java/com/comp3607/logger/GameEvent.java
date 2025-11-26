package com.comp3607.logger;

import java.time.LocalDateTime;
import com.comp3607.questions.Question;

public class GameEvent {
    private final String caseId;
    private final String playerName;
    private final String activity;
    private final LocalDateTime timestamp;
    private final String category;
    private final int questionValue;
    private final String answerGiven;
    private final String result;
    private final int scoreAfter;
    private final Question question;

    public GameEvent(String caseId, String playerName, String activity, LocalDateTime timestamp,
                     Question question, String answerGiven, String result, int scoreAfter) {
        this.caseId = caseId;
        this.playerName = playerName;
        this.activity = activity;
        this.timestamp = timestamp;
        this.question = question;

        if (question != null) {
            this.category = question.getCategory();
            this.questionValue = question.getValue();
        } else {
            this.category = null;
            this.questionValue = 0;
        }

        this.answerGiven = answerGiven;
        this.result = result;
        this.scoreAfter = scoreAfter;
    }

    // Getters
    public String getCaseId() { return caseId; }
    public String getPlayerName() { return playerName; }
    public String getActivity() { return activity; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getCategory() { return category; }
    public int getQuestionValue() { return questionValue; }
    public String getAnswerGiven() { return answerGiven; }
    public String getResult() { return result; }
    public int getScoreAfter() { return scoreAfter; }
    public Question getQuestion() { return question; }
}
