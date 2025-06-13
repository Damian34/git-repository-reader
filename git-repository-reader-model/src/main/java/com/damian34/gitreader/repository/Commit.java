package com.damian34.gitreader.repository;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Information about a Git repository commit")
public class Commit {
    @Schema(description = "Commit identifier (hash)", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;

    @Schema(description = "Commit message", requiredMode = Schema.RequiredMode.REQUIRED)
    private String message;

    @Schema(description = "Commit author", requiredMode = Schema.RequiredMode.REQUIRED)
    private String author;

    @Schema(description = "Commit creation date", requiredMode = Schema.RequiredMode.REQUIRED)
    private String date;
}