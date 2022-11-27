package com.dev1093.tictactoe.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidChoiceException extends RuntimeException
{
    public InvalidChoiceException (String message)
    {
        super(message);
    }
}
