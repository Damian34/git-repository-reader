package com.damian34.gitreader.domain.dto;

import com.damian34.gitreader.model.ExceptionDetails;
import com.damian34.gitreader.model.ProcessStatus;
import com.damian34.gitreader.model.repository.Branch;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Informacje o repozytorium Git", requiredProperties = {"url", "status"})
public class GitRepositoryDto {
    @Schema(description = "URL repozytorium Git")
    private String url;
    
    @Schema(description = "Status przetwarzania repozytorium")
    private ProcessStatus status;
    
    @Schema(description = "Lista gałęzi repozytorium")
    private List<Branch> branches;
    
    @Schema(description = "Szczegóły wyjątku w przypadku niepowodzenia")
    private ExceptionDetails exception;
}
