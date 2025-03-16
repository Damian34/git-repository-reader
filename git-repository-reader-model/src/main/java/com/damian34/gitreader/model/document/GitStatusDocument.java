package com.damian34.gitreader.model.document;

import com.damian34.gitreader.model.ExceptionDetails;
import com.damian34.gitreader.model.ProcessStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("git_statues")
public class GitStatusDocument {
    @Id
    private String url;
    private ProcessStatus status;
    private ExceptionDetails exception;
}
