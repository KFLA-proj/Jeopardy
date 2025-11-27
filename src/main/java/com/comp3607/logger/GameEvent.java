package com.comp3607.logger;

import java.time.LocalDateTime;
import com.comp3607.questions.Question;


/**
 * Represents an event that occurs during the game, capturing relevant details for logging purposes
 */
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

    /**
     * Creates a GameEvent object containing all of the details of an event occurring in the game
     * @param caseId the id of the event
     * @param playerName the name of the player who triggered the event
     * @param activity the activity which takes place upon trigger
     * @param timestamp the time at which the event occurred
     * @param question the question which the event was triggered upon
     * @param answerGiven the answer given to the question selected
     * @param result the resolution of the answer checked against the question
     * @param scoreAfter the updated score after the event resolves
     */
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
    /**
     * Returns the caseID for an event
     * @return the caseID as a string
     */
    public String getCaseId() { return caseId; }
    /**
     * Returns the name of the player who triggered the event
     * @return the player name as a string
     */
    public String getPlayerName() { return playerName; }
    /**
     * Returns the activity which took place upon trigger
     * @return the activity as a string
     */
    public String getActivity() { return activity; }
    /**
     * Returns the timestamp of when the event occurred
     * @return the LocalDateTime object containing the timestamp
     */
    public LocalDateTime getTimestamp() { return timestamp; }
    /**
     * Returns the category of the question involved in the event
     * @return the question category as a string
     */
    public String getCategory() { return category; }
    /**
     * Returns the value of the question involved in the event
     * @return the value of the question as an integer
     */
    public int getQuestionValue() { return questionValue; }
    /**
     * Returns the answer given to the question involved in the event
     * @return the answer given as a string
     */
    public String getAnswerGiven() { return answerGiven; }
    /**
     * Returns the result of checking the answer against the question
     * @return the result as a string
     */
    public String getResult() { return result; }
    /**
     * Returns the score after the event has resolved
     * @return the score as an integer
     */
    public int getScoreAfter() { return scoreAfter; }
    /**
     * Returns the question involved in the event
     * @return the Question object
     */
    public Question getQuestion() { return question; }
}
