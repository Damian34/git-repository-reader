package com.damian34.gitreader.api.protocol.reqeust;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dane uwierzytelniające do repozytorium Git", requiredProperties = {"url"})
public class GitCredentials {
    @Schema(description = "URL repozytorium Git", example = "https://github.com/Damian34/git-repository-reader")
    @NotBlank(message = "URL cannot be blank")
    private String url;
    
    @Schema(description = "Nazwa użytkownika do uwierzytelnienia (opcjonalne)", example = "username")
    private String username;
    
    @Schema(description = "Hasło do uwierzytelnienia (opcjonalne)", example = "password")
    private String password;
    
    @Schema(description = "Token dostępu do repozytorium (opcjonalne)", example = "ghp_123abc456def")
    private String token;
}