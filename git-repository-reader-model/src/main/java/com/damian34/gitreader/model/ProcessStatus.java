package com.damian34.gitreader.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Status przetwarzania repozytorium", enumAsRef = true)
public enum ProcessStatus {
    @Schema(description = "Oczekiwanie na przetworzenie")
    WAITING, 
    
    @Schema(description = "Przetwarzanie nie powiodło się")
    FAILED, 
    
    @Schema(description = "Przetwarzanie zakończone sukcesem")
    COMPLETED
}
