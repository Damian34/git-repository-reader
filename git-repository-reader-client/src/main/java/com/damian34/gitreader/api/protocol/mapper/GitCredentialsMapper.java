package com.damian34.gitreader.api.protocol.mapper;

import com.damian34.gitreader.api.protocol.reqeust.GitCredentialsRequest;
import com.damian34.gitreader.model.queue.GitConnectionCredentials;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GitCredentialsMapper {
    GitCredentialsMapper INSTANCE = Mappers.getMapper(GitCredentialsMapper.class);

    GitConnectionCredentials mapRequest(GitCredentialsRequest request);
}
