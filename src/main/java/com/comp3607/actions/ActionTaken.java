package com.comp3607.actions;

import com.comp3607.core_game.Game;

public interface ActionTaken {
    void execute(Game game);
    String info();
}
