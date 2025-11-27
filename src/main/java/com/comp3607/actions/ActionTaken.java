package com.comp3607.actions;

import com.comp3607.core_game.Game;
/**
 * Represents an action taken within a Jeopardy turn
 */
public interface ActionTaken {
    /**
     * Executes the action on the given game state
     * @param game the current game
     */
    void execute(Game game);
    /**
     * Provides information about the action taken
     * @return a string describing the action
     */
    String info();
}
