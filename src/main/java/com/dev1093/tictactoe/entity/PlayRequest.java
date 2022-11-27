package com.dev1093.tictactoe.entity;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotBlank;

public class PlayRequest
{
    /** Player ID **/
    @Size(max = 1, message = "Value should be 'X' or 'O'")
    @NotBlank
    private String playerId;

    /**
     * Position the player plays. The value ranges from 1-9.
     * Each value corresponds to the square number in the tic-tac-toe box.
     */
    @Max(9)
    @Min(1)
    @NotNull
    private Integer position;

    @Override public String toString ()
    {
        return "PlayRequest{" +
            "playerId='" + playerId + '\'' +
            ", position=" + position +
            '}';
    }

    public String getPlayerId ()
    {
        return playerId;
    }

    public void setPlayerId (String playerId)
    {
        this.playerId = playerId;
    }

    public Integer getPosition ()
    {
        return position;
    }

    public void setPosition (Integer position)
    {
        this.position = position;
    }
}
