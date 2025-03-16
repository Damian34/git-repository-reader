package com.damian34.gitreader.api.protocol.reqeust;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GitCredentialsRequest {
    @NotBlank(message = "URL cannot be blank")
    private String url;
    private String username;
    private String password;
    private String token;
}
