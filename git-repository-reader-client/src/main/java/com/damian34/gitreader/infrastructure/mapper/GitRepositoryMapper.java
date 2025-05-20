package com.damian34.gitreader.infrastructure.mapper;

import com.damian34.gitreader.domain.dto.GitRepositoryDto;
import com.damian34.gitreader.model.document.GitRepositoryDocument;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface GitRepositoryMapper {
    GitRepositoryMapper INSTANCE = Mappers.getMapper(GitRepositoryMapper.class);

    GitRepositoryDto documentToDto(GitRepositoryDocument document);

    List<GitRepositoryDto> toDtoList(List<GitRepositoryDocument> documents);
}
