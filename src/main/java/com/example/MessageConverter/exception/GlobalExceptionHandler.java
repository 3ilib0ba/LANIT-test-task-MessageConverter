package com.example.MessageConverter.exception;

import com.example.MessageConverter.model.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(InputFieldErrorException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorDto handleInputFieldError(InputFieldErrorException exception) {
        ErrorDto errorDTO = new ErrorDto(
                HttpStatus.BAD_REQUEST.name(), exception.getMessage(), LocalDateTime.now());
        return errorDTO;
    }

}
