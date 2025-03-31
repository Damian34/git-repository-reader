package com.damian34.gitreader.api.protocol.mapper;

import com.damian34.gitreader.api.protocol.reqeust.GitCredentials;
import com.damian34.gitreader.model.queue.GitConnectionCredentials;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface GitCredentialsMapper {
    GitCredentialsMapper INSTANCE = Mappers.getMapper(GitCredentialsMapper.class);

    GitConnectionCredentials mapCredentials(GitCredentials credentials);

    List<GitConnectionCredentials> mapCredentials(List<GitCredentials> credentials);
}
