package com.dev1093.tictactoe.controllers;

import com.dev1093.tictactoe.entity.PlayRequest;
import com.dev1093.tictactoe.entity.PlayResponse;
import com.dev1093.tictactoe.exceptions.InvalidChoiceException;
import com.dev1093.tictactoe.models.Player;
import com.dev1093.tictactoe.services.GameStateService;
import com.dev1093.tictactoe.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.dev1093.tictactoe.constants.Constants.*;

@RestController
@Configuration
public class GameController
{
    /** GameStateService instance **/
    @Autowired
    private GameStateService gameStateService;
    /** PlayerService instance **/
    @Autowired
    private PlayerService playerService;

    private final static Logger LOGGER =
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    @GetMapping("/players")
    public List<Player> getPlayers () {
        return playerService.getPlayers();
    }

    @GetMapping("/player")
    public Player getPlayer (@RequestParam(value = "id", defaultValue = "") String id) {
        return playerService.getPlayer(id);
    }

    @GetMapping("/state")
    public Map<String, String> getState() {
        return gameStateService.getBoard();
    }

    @PostMapping("/reset")
    public Map<String, String> resetState() {
         gameStateService.resetBoard();
         return gameStateService.getBoard();
    }

    @PostMapping("/play")
    public PlayResponse play (@RequestBody @Valid PlayRequest request)
    {
        final Player player = playerService.getPlayer(request.getPlayerId());

        if (gameStateService.isGameOver()) {
            LOGGER.log(Level.WARNING,GAME_OVER);
            throw new InvalidChoiceException(GAME_OVER);
        }

        if (!gameStateService.isGameStarted() && !(X.equals(player.getId()))) {
            LOGGER.log(Level.WARNING,X_ALWAYS_PLAYS_FIRST);
            throw new InvalidChoiceException(X_ALWAYS_PLAYS_FIRST);
        }
        else if (null != gameStateService.getPreviousPlayerId() &&
            gameStateService.getPreviousPlayerId().equals(player.getId())) {
            String nextPlayer = X.equals(player.getId()) ? O : X;
            throw new InvalidChoiceException("Player " + player.getId()
                + " already played the previous turn! Waiting for "
                + nextPlayer + " to play!");
        }
        gameStateService.updateState(String.valueOf(request.getPosition()), player);
        return new PlayResponse(findWinner(), gameStateService.getBoard());
    }

    private Player findWinner() {
        String winner = gameStateService.checkWinner();
        Player playerWinner;

        if (winner != null) {
            switch (winner) {
            case X:
            case O:
                playerWinner = playerService.getPlayer(winner);
                gameStateService.endGame(true);
                break;
            case DRAW:
                playerWinner = new Player(DRAW, DRAW_MESSAGE);
                gameStateService.endGame(true);
                break;
            default:
                playerWinner = null;
            }
            return playerWinner;
        }
        return null;
    }
}
