package com.damian34.gitreader.api.protocol.reqeust;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request for retrieving Git repository information")
public class GitUrlRequest {
    @Schema(description = "Git repository URL", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "URL cannot be blank")
    private String url;
}
