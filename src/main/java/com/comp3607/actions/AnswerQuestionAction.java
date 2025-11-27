package com.comp3607.actions;

import com.comp3607.core_game.Player;
import com.comp3607.questions.Question;
import com.comp3607.core_game.Game;
import com.comp3607.logger.GameLogger;

/**
 * represents an AnswerQuestionAction object within a Jeopardy turn
 */
public class AnswerQuestionAction implements ActionTaken {
    private Player player;
    private Question question;
    private char answer;

    /**
     * Creates a new AnswerQuestionAction object which represents a player answering a question within a round
     * @param player the player answering the question
     * @param question the question to be answered
     * @param answer the player's chosen answer
     */
    public AnswerQuestionAction(Player player, Question question, char answer) {
        this.player = player;
        this.question = question;
        this.answer = answer;
    }

    /**
     * Executes the AnswerQuestionAction object which will award points and deselect the question if the player answers correctly and log the action appropriately
     * @param game the specific instance of Jeopardy being played
     */
    @Override
    public void execute(Game game){
        player.recordAction(this);

        boolean correct = question.getAnswer() == answer;
        String result;
        if (correct) {
            result = "Correct";
        } else {
            result = "Incorrect";
        }

        if (correct){
            player.awardPoints(question.getValue());
            game.getVault().removeQuestion(question);
            game.dropActiveQuestion();
        }

        for (var obs : game.getObservers()) {
            if (obs instanceof GameLogger logger) {
                logger.logTurn(player, "Answer Question", question, String.valueOf(answer), result);
            }
        }
    }

    /**
     * A glorified toString used to gather info on a particular instance of a question being answered in a readable format.
     * @return the question's information
     */
    @Override
    public String info(){
        return player.getName() + " answered question: " + question.toString() + " with answer: " + answer + ".";
    }
}
