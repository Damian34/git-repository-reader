package com.damian34.gitreader.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExceptionDetails {
    private String type;
    private String message;
}
