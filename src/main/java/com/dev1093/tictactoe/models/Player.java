package com.dev1093.tictactoe.models;

public class Player
{
    /** Player ID **/
    private final String id;

    /** Player Name **/
    private final String name;

    public Player (String id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public String getId ()
    {
        return id;
    }

    public String getName ()
    {
        return name;
    }
}
