package com.damian34.gitreader.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Szczegóły wyjątku", requiredProperties = {"type", "message"})
public class ExceptionDetails {
    @Schema(description = "Typ wyjątku", example = "GitRepositoryNotFoundException")
    private String type;
    
    @Schema(description = "Wiadomość wyjątku", example = "Repository with URL 'https://github.com/nonexistent/repo' was not found")
    private String message;
}
