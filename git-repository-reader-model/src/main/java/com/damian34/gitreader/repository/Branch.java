package com.damian34.gitreader.repository;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Information about a Git repository branch")
public class Branch {
    @Schema(description = "Branch identifier", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;

    @Schema(description = "Branch name", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "List of commits in the branch")
    private List<Commit> commits;
}
