TicTacToe - Spring Boot application (2022-DEV1-093)

<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [How to run the application](#how-to-run-the-application)
- [API Endpoints](#api-endpoints)
    - [1. GET /state](#1-get-state)
    - [2. GET /players](#2-get-players)
    - [3. GET /player?id=X](#3-get-playeridx)
    - [4. POST /reset](#4-post-reset)
    - [5. POST /play](#5-post-play)
        - [Sample request](#sample-request)
        - [Sample response - Ongoing game](#sample-response---ongoing-game)
        - [Sample response - Game finished](#sample-response---game-completed)
- [Possible Future Enhancements](#possible-future-enhancements)

<!-- /TOC -->

This TicTacToe game has a grid of 3x3 i.e. total 9 squares. The player IDs are "X" and "O". 

This API exposes the below endpoints:

1. GET /state
2. GET /players
3. GET /player?id=X]
4. POST /reset
5. POST /play

# How to run the application

Install maven and java in your system.

Checkout the code and navigate to the project folder (`<project_path>/tictactoe`) in the terminal. 

Run the below maven command.
```bash
$ mvn spring-boot:run
```
Or alternatively, execute the `com.dev1093.tictactoe.TicTacToeApplication.java` class from your IDE (IntelliJ or Eclipse)

# API Endpoints

## 1. GET /state
Returns the current state of the game board as a Map of String key and values. 
_key_ is the position and _value_ is the playerId. The key value ranges from 1-9 each value corresponding to each square (total 9 squares) in the tic-tac-toe game board.

**Sample response**

Initial state is:
```json
{
    "1": null,
    "2": null,
    "3": null,
    "4": null,
    "5": null,
    "6": null,
    "7": null,
    "8": null,
    "9": null
}
```

## 2. GET /players
Returns a list of the available players.

**Sample response**
```json
[
    {
        "id": "X",
        "name": "Player 1"
    },
    {
        "id": "O",
        "name": "Player 2"
    }
]
``` 
## 3. GET /player?id=X
Returns the player corresponding to the id passed.

**Sample response**
```json
{
    "id": "X",
    "name": "Player 1"
}
```

## 4. POST /reset
Resets the game board to the initial state.

**Sample response**

```json
{
    "1": null,
    "2": null,
    "3": null,
    "4": null,
    "5": null,
    "6": null,
    "7": null,
    "8": null,
    "9": null
}
```

## 5. POST /play
Called every time a player plays on the game board.

### Sample request

There are validations on both fields used in the below POST payload.

```json
{
	"playerId" : "X",
	"position" : 5
}
```

### Sample response - Ongoing game

`gameOver` field will be false as long as the game is ongoing.
`winner` field indicates the winning player or draw result. Once we have a `winner`, `gameOver` will be true.
`state` field returns the game board after each turn.

```json
{
    "gameOver": false,
    "winner": null,
    "state": {
        "1": null,
        "2": null,
        "3": null,
        "4": null,
        "5": "X",
        "6": null,
        "7": null,
        "8": null,
        "9": null
    }
}
```

### Sample response - Game completed

- `gameOver` - true once there is a winner in the game.
- `winner` - contains the winner or draw.


```json
{
    "gameOver": true,
    "winner": {
        "id": "X",
        "name": "Player 1"
    },
    "state": {
        "0": "X",
        "1": "X",
        "2": "X",
        "3": "O",
        "4": "O",
        "5": null,
        "6": null,
        "7": null,
        "8": null,
        "9": null
    }
}
```

```json
{
    "gameOver": true,
    "winner": {
        "id": "Draw",
        "name": "The game is a draw! No one wins."
    },
    "state": {
        "1": "X",
        "2": "X",
        "3": "O",
        "4": "O",
        "5": "O",
        "6": "X",
        "7": "X",
        "8": "O",
        "9": "X"
    }
}
```

# Possible Future Enhancements

- Create a provision of creating and storing user profiles with records of wins and losses as each X or O player.
- User interface which invokes the APIs.
