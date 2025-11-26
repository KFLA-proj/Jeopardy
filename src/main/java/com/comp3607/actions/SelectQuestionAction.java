package com.comp3607.actions;

import com.comp3607.core_game.Player;
import com.comp3607.questions.Question;
import com.comp3607.core_game.Game;
import com.comp3607.core_game.GameObserver;
import com.comp3607.logger.GameLogger;


public class SelectQuestionAction implements ActionTaken {
    private Player player;
    private Question q;

    public SelectQuestionAction(Player player, Question question) {
        this.player = player;
        this.q = question;
    }

    @Override
    public void execute(Game game) {
        player.recordAction(this);
        game.setActiveQuestion(q);
        q.displayOptions();

        for (GameObserver obs : game.getObservers()) {
            if (obs instanceof GameLogger logger) {
                logger.logTurn(player, "Select Question", q, null, null);
            }
        }
    }

    @Override
    public String info() {
        return player.getName() + " selected question: " + q.toString() + ".";
    }
}
