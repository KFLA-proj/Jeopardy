package com.comp3607.logger;

import java.time.LocalDateTime;

import com.comp3607.questions.Question;

public class GameEvent {
    public final String caseId;
    public final String playerName;
    public final String activity;
    public final LocalDateTime timestamp;
    public final String category;
    public final int questionValue;
    public final String answerGiven;
    public final String result;
    public final int scoreAfter;
    public Question question;


    public GameEvent(String caseId, String playerName, String activity, LocalDateTime timestamp,
                    Question question, String answerGiven, String result, int scoreAfter) {
        this.caseId = caseId;
        this.playerName = playerName;
        this.activity = activity;
        this.timestamp = timestamp;
        this.question = question;                         // store the Question object
        this.category = (question != null) ? question.getCategory() : null;
        this.questionValue = (question != null) ? question.getValue() : 0;
        this.answerGiven = answerGiven;
        this.result = result;
        this.scoreAfter = scoreAfter;
    }
}
