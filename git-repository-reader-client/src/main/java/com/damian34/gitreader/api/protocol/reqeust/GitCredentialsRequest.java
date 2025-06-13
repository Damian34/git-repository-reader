package com.damian34.gitreader.api.protocol.reqeust;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request for processing Git repositories")
public class GitCredentialsRequest {
    @Schema(description = "List of Git repository authentication credentials", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "Credentials list cannot be empty")
    @Valid
    private List<GitCredentials> credentials;
}
