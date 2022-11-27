package com.dev1093.tictactoe.entity;

import com.dev1093.tictactoe.models.Player;

import java.util.Map;

public class PlayResponse
{
    /** Boolean flag to indicate if the game is over **/
    public Boolean gameOver = false;
    /** Winner of the game **/
    public Player winner;
    /**
     * Map representing the tic-tac-toe board state.
     * The key is the square number and the value is the Player id
     * or null if the player hasn't played in that position.
     */
    public Map<String, String> state;

    public PlayResponse (Player winner, Map<String, String> state)
    {
        if(null != winner) {
            this.winner = winner;
            this.gameOver = true;
        }
        this.state = state;
    }

    public Boolean getGameOver ()
    {
        return gameOver;
    }

    public void setGameOver (Boolean gameOver)
    {
        this.gameOver = gameOver;
    }

    public Map<String, String> getState ()
    {
        return state;
    }

    public void setState (Map<String, String> state)
    {
        this.state = state;
    }
}
