package com.damian34.gitreader.model.repository;

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
@Schema(description = "Informacje o gałęzi repozytorium Git", requiredProperties = {"id", "name"})
public class Branch {
    @Schema(description = "Identyfikator gałęzi", example = "a1b2c3d4e5f6")
    private String id;
    
    @Schema(description = "Nazwa gałęzi", example = "refs/remotes/origin/main")
    private String name;
    
    @Schema(description = "Lista commitów w gałęzi")
    private List<Commit> commits;
}
