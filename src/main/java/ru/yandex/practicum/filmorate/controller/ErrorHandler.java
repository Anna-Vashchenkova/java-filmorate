package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final DataNotFoundException e) {
        log.info("Ошибка данных");
        return new ErrorResponse("Ошибка данных", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handle(final Throwable e) {
        log.warn("Произошла непредвиденная ошибка", e);
        return new ErrorResponse("Произошла непредвиденная ошибка", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidEmail(final ValidationException e) {
        log.info("Ошибка валидации");
        return new ErrorResponse(
                "Ошибка валидации", e.getMessage()
        );
    }
}
