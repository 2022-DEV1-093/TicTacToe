package com.dev1093.tictactoe.controller;

import com.dev1093.tictactoe.controllers.GameController;
import com.dev1093.tictactoe.exceptions.InvalidChoiceException;
import com.dev1093.tictactoe.exceptions.PlayerNotFoundException;
import com.dev1093.tictactoe.services.GameStateService;
import com.dev1093.tictactoe.services.PlayerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static com.dev1093.tictactoe.constants.Constants.*;

@WebMvcTest(value = GameController.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = { GameController.class, GameStateService.class, PlayerService.class })
public class GameControllerTest
{
    /** GameStateService MockBean instance **/
    @MockBean
    private GameStateService gameStateService;

    /** PlayerService MockBean instance **/
    @MockBean
    private PlayerService playerService;

    /** Mockmvc instance **/
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp ()
    {
        GameStateService gameStateService1 = new GameStateService();
        PlayerService playerService1 = new PlayerService();
        ReflectionTestUtils.setField(playerService, "players", playerService1.getPlayers());
        ReflectionTestUtils.setField(gameStateService, "board", gameStateService1.getBoard());
        when(playerService.getPlayers()).thenCallRealMethod();
        when(playerService.getPlayer(anyString())).thenCallRealMethod();
        when(gameStateService.getBoard()).thenCallRealMethod();
        doCallRealMethod().when(gameStateService).resetBoard();
    }


    @Test
    public void testGetPlayersList () throws Exception
    {
        mockMvc.perform(get("/players")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(
                "[{\"id\":\"X\",\"name\":\"Player 1\"},{\"id\":\"O\",\"name\":\"Player 2\"}]"));
    }

    @Test
    public void testGetPlayer () throws Exception
    {
        mockMvc.perform(get("/player").param("id",X)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(
                "{\"id\":\"X\",\"name\":\"Player 1\"}"));
    }

    @Test
    public void testGameBoardState () throws Exception
    {
        mockMvc.perform(get("/state")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(
                "{\"1\":null,\"2\":null,\"3\":null,\"4\":null,\"5\":null,\"6\":null,\"7\":null,\"8\":null,\"9\":null}"));
    }

    @Test
    public void testGameBoardReset () throws Exception
    {
        mockMvc.perform(post("/reset")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(
                "{\"1\":null,\"2\":null,\"3\":null,\"4\":null,\"5\":null,\"6\":null,\"7\":null,\"8\":null,\"9\":null}"));
    }

    @Test
    public void testPlay () throws Exception
    {
        doCallRealMethod().when(gameStateService).updateState(anyString(), any());
        mockMvc.perform(post("/play").content("{\n"
                    + "\t\"playerId\" : \"X\",\n"
                    + "\t\"position\" : 1\n"
                    + "}")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(
                "{\"gameOver\":false,"
                    + "\"winner\":null,"
                    + "\"state\":"
                    + "{\"1\":\"X\","
                    + "\"2\":null,"
                    + "\"3\":null,"
                    + "\"4\":null,"
                    + "\"5\":null,"
                    + "\"6\":null,"
                    + "\"7\":null,"
                    + "\"8\":null,"
                    + "\"9\":null}}"));
    }

    @Test
    public void testXPlaysFirstThrowException () throws Exception
    {
        doCallRealMethod().when(gameStateService).updateState(anyString(), any());
        mockMvc.perform(post("/play").content("{\n"
                    + "\t\"playerId\" : \"O\",\n"
                    + "\t\"position\" : 5\n"
                    + "}")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(response -> assertTrue(response.getResolvedException() instanceof InvalidChoiceException))
            .andExpect(response -> assertEquals(X_ALWAYS_PLAYS_FIRST, Objects.requireNonNull(
                response.getResolvedException()).getMessage()));
    }

    @Test
    public void testGameOverThrowException () throws Exception
    {
        when(gameStateService.isGameOver()).thenReturn(true);
        mockMvc.perform(post("/play").content("{\n"
                    + "\t\"playerId\" : \"X\",\n"
                    + "\t\"position\" : 5\n"
                    + "}")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(response -> assertTrue(response.getResolvedException() instanceof InvalidChoiceException))
            .andExpect(response -> assertEquals(GAME_OVER, Objects.requireNonNull(
                response.getResolvedException()).getMessage()));
    }

    @Test
    public void testTakenPositionThrowException () throws Exception
    {
        gameStateService.getBoard().put("5", X);
        doCallRealMethod().when(gameStateService).updateState(anyString(), any());
        mockMvc.perform(post("/play").content("{\n"
                    + "\t\"playerId\" : \"X\",\n"
                    + "\t\"position\" : 5\n"
                    + "}")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(response -> assertTrue(response.getResolvedException() instanceof InvalidChoiceException))
            .andExpect(response -> assertEquals(POSITION_TAKEN, Objects.requireNonNull(
                response.getResolvedException()).getMessage()));
    }

    @Test
    public void testInvalidPlayerThrowException () throws Exception
    {
        doCallRealMethod().when(gameStateService).updateState(anyString(), any());
        mockMvc.perform(post("/play").content("{\n"
                    + "\t\"playerId\" : \"W\",\n"
                    + "\t\"position\" : 5\n"
                    + "}")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(response -> assertTrue(response.getResolvedException() instanceof PlayerNotFoundException))
            .andExpect(response -> assertEquals("Player W not found", Objects.requireNonNull(
                response.getResolvedException()).getMessage()));
    }

    @Test
    public void testWinner () throws Exception
    {
        Map<String, String> boardMap = new HashMap<>();
        boardMap.put("1", "X");
        boardMap.put("2", "O");
        boardMap.put("3", "O");
        boardMap.put("4", null);
        boardMap.put("5", "X");
        boardMap.put("6", null);
        boardMap.put("7", null);
        boardMap.put("8", null);
        boardMap.put("9", null);
        ReflectionTestUtils.setField(gameStateService, "board", boardMap);
        when(gameStateService.checkWinner()).thenCallRealMethod();
        doCallRealMethod().when(gameStateService).endGame(anyBoolean());
        doCallRealMethod().when(gameStateService).updateState(anyString(), any());
        mockMvc.perform(post("/play").content("{\n"
                    + "\t\"playerId\" : \"X\",\n"
                    + "\t\"position\" : 9\n"
                    + "}")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(
                "{\"gameOver\":true,"
                    + "\"winner\":{\"id\":\"X\",\"name\":\"Player 1\"},"
                    + "\"state\":{\"1\":\"X\",\"2\":\"O\",\"3\":\"O\",\"4\":null,"
                    + "\"5\":\"X\",\"6\":null,\"7\":null,\"8\":null,\"9\":\"X\"}}"));
    }

    @Test
    public void testDraw () throws Exception
    {
        Map<String, String> boardMap = new HashMap<>();
        boardMap.put("1", "X");
        boardMap.put("2", "X");
        boardMap.put("3", "O");
        boardMap.put("4", "O");
        boardMap.put("5", "O");
        boardMap.put("6", "X");
        boardMap.put("7", "X");
        boardMap.put("8", "O");
        boardMap.put("9", null);
        ReflectionTestUtils.setField(gameStateService, "board", boardMap);
        when(gameStateService.checkWinner()).thenCallRealMethod();
        doCallRealMethod().when(gameStateService).endGame(anyBoolean());
        doCallRealMethod().when(gameStateService).updateState(anyString(), any());
        mockMvc.perform(post("/play").content("{\n"
                    + "\t\"playerId\" : \"X\",\n"
                    + "\t\"position\" : 9\n"
                    + "}")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(
                "{\"gameOver\":true,"
                    + "\"winner\":{\"id\":\"Draw\","
                    + "\"name\":\"The game is a draw! No one wins.\"},"
                    + "\"state\":{\"1\":\"X\",\"2\":\"X\",\"3\":\"O\",\"4\":\"O\","
                    + "\"5\":\"O\",\"6\":\"X\",\"7\":\"X\",\"8\":\"O\",\"9\":\"X\"}}"));
    }
}
