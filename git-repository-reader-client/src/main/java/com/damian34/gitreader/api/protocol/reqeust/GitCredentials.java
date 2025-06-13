package com.damian34.gitreader.api.protocol.reqeust;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Git repository authentication credentials")
public class GitCredentials {
    @Schema(description = "Git repository URL", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "URL cannot be blank")
    private String url;
    
    @Schema(description = "Username for authentication (optional)")
    private String username;
    
    @Schema(description = "Password for authentication (optional)")
    private String password;
    
    @Schema(description = "Access token for repository (optional)")
    private String token;
}