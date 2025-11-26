package com.comp3607.actions;

import com.comp3607.core_game.Player;
import com.comp3607.questions.Question;
import com.comp3607.core_game.Game;
import com.comp3607.logger.GameLogger;

public class AnswerQuestionAction implements ActionTaken {
    private Player player;
    private Question question;
    private char answer;

    public AnswerQuestionAction(Player player, Question question, char answer) {
        this.player = player;
        this.question = question;
        this.answer = answer;
    }

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

    @Override
    public String info(){
        return player.getName() + " answered question: " + question.toString() + " with answer: " + answer + ".";
    }
}
