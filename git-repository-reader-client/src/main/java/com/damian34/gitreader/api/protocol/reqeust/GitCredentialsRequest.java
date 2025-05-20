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
@Schema(description = "Zapytanie do przetworzenia repozytoriów Git", requiredProperties = {"credentials"})
public class GitCredentialsRequest {
    @Schema(description = "Lista danych uwierzytelniających do repozytoriów Git")
    @Valid
    @NotEmpty(message = "Credentials list cannot be empty")
    private List<GitCredentials> credentials;
}
