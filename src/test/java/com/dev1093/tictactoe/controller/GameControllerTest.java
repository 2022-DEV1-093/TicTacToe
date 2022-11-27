package com.dev1093.tictactoe.controller;

import com.dev1093.tictactoe.controllers.GameController;
import com.dev1093.tictactoe.exceptions.InvalidChoiceException;
import com.dev1093.tictactoe.exceptions.PlayerNotFoundException;
import com.dev1093.tictactoe.services.GameStateService;
import com.dev1093.tictactoe.services.PlayerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.junit.jupiter.api.Assertions.assertTrue;

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



    @Test
    public void testPlayersList () throws Exception
    {
        mockMvc.perform(get("/players")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(
                "expectedJsonString"));
    }

    @Test
    public void testPlayer () throws Exception
    {
        mockMvc.perform(get("/player").param("id","X")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(
                "expectedJsonString"));
    }

    @Test
    public void testState () throws Exception
    {
        mockMvc.perform(get("/state")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(
                "expectedJsonString"));
    }

    @Test
    public void testReset () throws Exception
    {
        mockMvc.perform(post("/reset")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(
                "expectedJsonString"));
    }

    @Test
    public void testPlay () throws Exception
    {
        mockMvc.perform(post("/play").content("{\n"
                    + "\t\"playerId\" : \"X\",\n"
                    + "\t\"position\" : 1\n"
                    + "}")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(
                "expectedJsonString"));
    }

    @Test
    public void testPlayWithOFirstThrowException () throws Exception
    {
        mockMvc.perform(post("/play").content("{\n"
                    + "\t\"playerId\" : \"O\",\n"
                    + "\t\"position\" : 5\n"
                    + "}")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(response -> assertTrue(response.getResolvedException() instanceof InvalidChoiceException))
            .andExpect(response -> assertEquals("X always goes first!", response.getResolvedException().getMessage()));
    }

    @Test
    public void testPlayOnTakenPositionThrowException () throws Exception
    {
        mockMvc.perform(post("/play").content("{\n"
                    + "\t\"playerId\" : \"X\",\n"
                    + "\t\"position\" : 5\n"
                    + "}")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(response -> assertTrue(response.getResolvedException() instanceof InvalidChoiceException))
            .andExpect(response -> assertEquals("The position has already been taken", response.getResolvedException().getMessage()));
    }

    @Test
    public void testPlayWithInvalidPlayerThrowException () throws Exception
    {
        mockMvc.perform(post("/play").content("{\n"
                    + "\t\"playerId\" : \"W\",\n"
                    + "\t\"position\" : 5\n"
                    + "}")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(response -> assertTrue(response.getResolvedException() instanceof PlayerNotFoundException))
            .andExpect(response -> assertEquals("Player W not found", response.getResolvedException().getMessage()));
    }

    @Test
    public void testPlayWinner () throws Exception
    {
        mockMvc.perform(post("/play").content("{\n"
                    + "\t\"playerId\" : \"X\",\n"
                    + "\t\"position\" : 9\n"
                    + "}")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(
                "expectedJsonString"));
    }

    @Test
    public void testPlayDraw () throws Exception
    {

        mockMvc.perform(post("/play").content("{\n"
                    + "\t\"playerId\" : \"X\",\n"
                    + "\t\"position\" : 9\n"
                    + "}")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(
                "expectedJsonString"));
    }
}
