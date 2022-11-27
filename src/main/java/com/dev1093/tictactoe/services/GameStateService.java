package com.dev1093.tictactoe.services;

import com.dev1093.tictactoe.exceptions.InvalidChoiceException;
import com.dev1093.tictactoe.models.Player;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.dev1093.tictactoe.constants.Constants.*;

@Service
public class GameStateService
{
    /** Boolean flag to indicate if the game is over **/
    private boolean gameOver;
    /** Map to indicate the tic-tac-toe game board **/
    private final Map<String, String> board;
    /** Boolean flag to indicate if the game has started **/
    private boolean gameStarted;
    /** Stores the player id of the last move**/
    private String previousPlayerId;

    public GameStateService ()
    {
        this.board = new HashMap<>();
        resetBoard();
    }

    public boolean isGameOver ()
    {
        return gameOver;
    }

    public Map<String, String> getBoard ()
    {
        return board;
    }

    public boolean isGameStarted ()
    {
        return gameStarted;
    }

    public String getPreviousPlayerId ()
    {
        return previousPlayerId;
    }

    public void endGame(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void updateState(String position, Player player) {
        if (this.board.get(position) != null) {
            throw new InvalidChoiceException(POSITION_TAKEN);
        }
        this.board.put(position, player.getId());
        this.previousPlayerId = player.getId();
        this.gameStarted = true;
    }

    public String checkWinner() {
        for (int a = 0; a < 8; a++) {
            String line = null;
            switch (a) {
            case 0:
                line = this.board.get("1") + this.board.get("2") + this.board.get("3");
                break;
            case 1:
                line = this.board.get("4") + this.board.get("5") + this.board.get("6");
                break;
            case 2:
                line = this.board.get("7") + this.board.get("8") + this.board.get("9");
                break;
            case 3:
                line = this.board.get("1") + this.board.get("4") + this.board.get("7");
                break;
            case 4:
                line = this.board.get("2") + this.board.get("5") + this.board.get("8");
                break;
            case 5:
                line = this.board.get("3") + this.board.get("6") + this.board.get("9");
                break;
            case 6:
                line = this.board.get("1") + this.board.get("5") + this.board.get("9");
                break;
            case 7:
                line = this.board.get("3") + this.board.get("5") + this.board.get("7");
                break;
            default:
                break;
            }
            if (X_WINS.equals(line)) {
                return X;
            } else if (O_WINS.equals(line)) {
                return O;
            }
        }

        if(this.board.values().stream().filter(Objects::nonNull).collect(Collectors.toList()).size() == 9) {
            return DRAW;
        }
        return null;
    }

    public void resetBoard() {
        this.board.put("1", null);
        this.board.put("2", null);
        this.board.put("3", null);
        this.board.put("4", null);
        this.board.put("5", null);
        this.board.put("6", null);
        this.board.put("7", null);
        this.board.put("8", null);
        this.board.put("9", null);
        this.gameOver = false;
        this.gameStarted = false;
        this.previousPlayerId = null;
    }
}
