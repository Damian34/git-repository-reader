package com.damian34.gitreader.model.document;

import com.damian34.gitreader.model.ExceptionDetails;
import com.damian34.gitreader.model.ProcessStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@AllArgsConstructor
@Document("git_statues")
public class GitStatusDocument {
    @MongoId
    private String url;
    private ProcessStatus status;
    private ExceptionDetails exception;
}
