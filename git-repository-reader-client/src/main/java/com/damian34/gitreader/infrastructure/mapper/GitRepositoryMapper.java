package com.damian34.gitreader.infrastructure.mapper;

import com.damian34.gitreader.domain.dto.GitRepositoryDto;
import com.damian34.gitreader.model.document.GitStatusDocument;
import com.damian34.gitreader.model.repository.Branch;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface GitRepositoryMapper {
    GitRepositoryMapper INSTANCE = Mappers.getMapper(GitRepositoryMapper.class);

    @Mapping(source = "document.url", target = "url")
    @Mapping(source = "document.status", target = "status")
    @Mapping(source = "document.exception", target = "exception")
    @Mapping(source = "branches", target = "branches")
    GitRepositoryDto documentToDto(GitStatusDocument document, List<Branch> branches);

}
