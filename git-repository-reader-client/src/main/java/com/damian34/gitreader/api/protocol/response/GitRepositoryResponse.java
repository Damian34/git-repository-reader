package com.damian34.gitreader.api.protocol.response;

import com.damian34.gitreader.ExceptionDetails;
import com.damian34.gitreader.ProcessStatus;
import com.damian34.gitreader.repository.Branch;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Git repository information")
public class GitRepositoryResponse {
    @Schema(description = "Git repository URL", requiredMode = Schema.RequiredMode.REQUIRED)
    private String url;
    
    @Schema(description = "Repository processing status", requiredMode = Schema.RequiredMode.REQUIRED)
    private ProcessStatus status;
    
    @Schema(description = "List of repository branches")
    private List<Branch> branches;
    
    @Schema(description = "Exception details in case of failure")
    private ExceptionDetails exception;
}