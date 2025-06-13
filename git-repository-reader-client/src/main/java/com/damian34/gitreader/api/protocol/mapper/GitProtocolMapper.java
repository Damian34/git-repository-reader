package com.damian34.gitreader.api.protocol.mapper;

import com.damian34.gitreader.api.protocol.reqeust.GitCredentials;
import com.damian34.gitreader.api.protocol.response.GitRepositoryResponse;
import com.damian34.gitreader.domain.dto.GitRepositoryDto;
import com.damian34.gitreader.queue.GitConnectionCredentials;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GitProtocolMapper {
    List<GitConnectionCredentials> mapCredentials(List<GitCredentials> credentials);

    GitRepositoryResponse toResponse(GitRepositoryDto dto);

    List<GitRepositoryResponse> toResponseList(List<GitRepositoryDto> dtos);
}
