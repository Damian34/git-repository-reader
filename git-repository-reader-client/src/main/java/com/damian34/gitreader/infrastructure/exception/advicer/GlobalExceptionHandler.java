package com.damian34.gitreader.infrastructure.exception.advicer;

import com.damian34.gitreader.domain.exception.GitRepositoryDeleteException;
import com.damian34.gitreader.infrastructure.exception.NotFoundGitRepositoryException;
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

    @ExceptionHandler(GitRepositoryDeleteException.class)
    public ResponseEntity<String> handleGitRepositoryDeleteException(GitRepositoryDeleteException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }
}
