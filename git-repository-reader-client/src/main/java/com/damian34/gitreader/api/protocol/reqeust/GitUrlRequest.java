package com.damian34.gitreader.api.protocol.reqeust;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Zapytanie o pobieranie repozytorium Git", requiredProperties = {"url"})
public class GitUrlRequest {
    @Schema(description = "URL repozytorium Git", example = "https://github.com/Damian34/git-repository-reader")
    @NotBlank(message = "URL cannot be blank")
    private String url;
}
