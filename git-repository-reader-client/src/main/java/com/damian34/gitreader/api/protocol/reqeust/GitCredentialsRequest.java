package com.damian34.gitreader.api.protocol.reqeust;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GitCredentialsRequest {
    @Valid
    @NotEmpty(message = "Credentials list cannot be empty")
    private List<GitCredentials> credentials;
}
