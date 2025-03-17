package com.damian34.gitreader.infrastructure.mapper;

import com.damian34.gitreader.domain.dto.GitStatusDto;
import com.damian34.gitreader.model.document.GitStatusDocument;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GitStatusMapper {
    GitStatusMapper INSTANCE = Mappers.getMapper(GitStatusMapper.class);

    GitStatusDto documentToDto(GitStatusDocument document);
}
