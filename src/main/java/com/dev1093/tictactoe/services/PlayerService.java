package com.dev1093.tictactoe.services;

import com.dev1093.tictactoe.exceptions.PlayerNotFoundException;
import com.dev1093.tictactoe.models.Player;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlayerService
{
    /** List of players **/
    List<Player> players;

    public PlayerService ()
    {
        players = new ArrayList<>(2);
        players.add(new Player("X", "Player 1"));
        players.add(new Player("O", "Player 2"));
    }

    public List<Player> getPlayers ()
    {
        return players;
    }

    public Player getPlayer(String player) {
        List<Player> collect = players.stream()
            .filter(e -> e.getId().equals(player))
            .collect(Collectors.toList());
        if (!collect.isEmpty()) {
            return collect.get(0);
        }
        throw new PlayerNotFoundException("Player " + player + " not found");
    }
}
