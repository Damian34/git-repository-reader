package com.damian34.gitreader.model.repository;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Informacje o commicie w repozytorium Git", requiredProperties = {"id", "message", "author", "date"})
public class Commit {
    @Schema(description = "Identyfikator commita (hash)", example = "a1b2c3d4e5f6")
    private String id;
    
    @Schema(description = "Wiadomość commita", example = "Initial commit")
    private String message;
    
    @Schema(description = "Autor commita", example = "Damian Wójcik")
    private String author;
    
    @Schema(description = "Data utworzenia commita", example = "2023-01-01T12:00:00+00:00")
    private String date;
}