package com.comp3607.actions;

import com.comp3607.core_game.Player;
import com.comp3607.questions.Question;
import com.comp3607.core_game.Game;
//import com.comp3607.questions.Vault;


public class SelectQuestionAction implements ActionTaken{
    private Player player;
    private Question q;

    public SelectQuestionAction(Player player, Question question) {
        this.player = player;
        this.q = question;
    }

    @Override
    public void execute(Game game){
        player.recordAction(this);
        game.setActiveQuestion(q);
        q.displayOptions();
        
    }

    @Override
    public String info(){
        return player.getName() + " selected question: " + q.toString() + ".";
    }
}
