package com.comp3607.actions;

import com.comp3607.core_game.Player;
import com.comp3607.questions.Question;
import com.comp3607.core_game.Game;
import com.comp3607.core_game.GameObserver;
import com.comp3607.logger.GameLogger;

/**
 * Represents the action of a question being selected in the game of Jeopardy
 */
public class SelectQuestionAction implements ActionTaken {
    private Player player;
    private Question q;

    /**
     * Creates a new SelectQuestionAction object which represents a player choosing a particular question within a round of Jeopardy
     * @param player the player choosing the question
     * @param question the question being chosen
     */
    public SelectQuestionAction(Player player, Question question) {
        this.player = player;
        this.q = question;
    }

    /**
     * Executes the SelectQuestionAction object which sets a question as active in the game instance and displays the available options to choose from for the question, also logs appropriately.
     * @param game the specific instance of Jeopardy being played
     */
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

    /**
     * A glorified toString used to gather info on a particular instance of a question being answered in a readable format.
     * @return the question's information
     */
    @Override
    public String info() {
        return player.getName() + " selected question: " + q.toString() + ".";
    }
}
