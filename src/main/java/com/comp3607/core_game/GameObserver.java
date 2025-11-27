package com.comp3607.core_game;

/**
 * Represents an observer within the game of Jeopardy
 */
public interface GameObserver {
    /**
     * Updates the observer with a message
     * @param message The message to update the observer with
     */
    void update(String message);
}
