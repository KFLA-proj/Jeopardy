package com.comp3607.actions;

import com.comp3607.core_game.Player;
import com.comp3607.questions.Question;
import com.comp3607.core_game.Game;

public class AnswerQuestionAction implements ActionTaken{
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
        if (question.getAnswer() == answer){
            player.awardPoints(question.getValue());
            game.getVault().removeQuestion(question);
            game.dropActiveQuestion();
        }
    }

    @Override
    public String info(){
        return player.getName() + " answered question: " + question.toString() + " with answer: " + answer + ".";
    }
}
