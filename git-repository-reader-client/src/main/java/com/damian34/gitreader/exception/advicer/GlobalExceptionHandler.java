package com.damian34.gitreader.exception.advicer;

import com.damian34.gitreader.exception.NotFoundGitRepositoryException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundGitRepositoryException.class)
    public ResponseEntity<String> handleNotFoundGitRepositoryException(NotFoundGitRepositoryException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
