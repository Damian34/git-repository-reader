package com.damian34.gitreader.infrastructure.service;

import com.damian34.gitreader.config.TestContainerKafkaInitializer;
import com.damian34.gitreader.config.TestContainerMongoInitializer;
import com.damian34.gitreader.domain.dto.GitRepositoryDto;
import com.damian34.gitreader.infrastructure.exception.NotFoundGitRepositoryException;
import com.damian34.gitreader.infrastructure.repository.GitRepositoryDocumentRepository;
import com.damian34.gitreader.ProcessStatus;
import com.damian34.gitreader.document.GitRepositoryDocument;
import com.damian34.gitreader.repository.Branch;
import com.damian34.gitreader.repository.Commit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@ContextConfiguration(initializers = {
        TestContainerMongoInitializer.class,
        TestContainerKafkaInitializer.class
})
class GitRepositoryPersistenceServiceImplTest {

    private static final String TEST_URL = "https://github.com/Damian34/git-repository-reader.git";

    @MockitoBean
    private GitRepositoryDocumentRepository gitRepositoryDocumentRepository;

    @Autowired
    private GitRepositoryPersistenceServiceImpl gitRepositoryPersistenceService;

    private GitRepositoryDocument sampleRepositoryDocument;

    @BeforeEach
    void setUp() {
        Branch branch = new Branch("branchId", "main", 
                List.of(new Commit("commitId", "Initial commit", "Author", "2023-01-01")));
        sampleRepositoryDocument = new GitRepositoryDocument(TEST_URL, null, ProcessStatus.COMPLETED, null, List.of(branch));
    }

    @Test
    void shouldReturnDtoWhenRepositoryExists() {
        // given
        Mockito.when(gitRepositoryDocumentRepository.findById(TEST_URL)).thenReturn(Optional.of(sampleRepositoryDocument));

        // when
        GitRepositoryDto result = gitRepositoryPersistenceService.find(TEST_URL);

        // then
        Assertions.assertEquals(TEST_URL, result.getUrl());
        Assertions.assertEquals(1, result.getBranches().size());
        Assertions.assertEquals("main", result.getBranches().getFirst().getName());
    }

    @Test
    void shouldThrowExceptionWhenRepositoryNotFound() {
        // given
        Mockito.when(gitRepositoryDocumentRepository.findById(TEST_URL)).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThrows(NotFoundGitRepositoryException.class, 
                () -> gitRepositoryPersistenceService.find(TEST_URL));
    }

    @Test
    void shouldReturnRepositoriesWhenDataExists() {
        // given
        Mockito.when(gitRepositoryDocumentRepository.findAll()).thenReturn(List.of(sampleRepositoryDocument));

        // when
        List<GitRepositoryDto> results = gitRepositoryPersistenceService.findAll();

        // then
        Assertions.assertEquals(1, results.size());
        Assertions.assertEquals(TEST_URL, results.getFirst().getUrl());
    }

    @Test
    void shouldReturnEmptyListWhenNoDataExists() {
        // given
        Mockito.when(gitRepositoryDocumentRepository.findAll()).thenReturn(Collections.emptyList());

        // when
        List<GitRepositoryDto> results = gitRepositoryPersistenceService.findAll();

        // then
        Assertions.assertTrue(results.isEmpty());
    }

    @Test
    void shouldDeleteRepositoryWhenExists() {
        // given
        Mockito.when(gitRepositoryDocumentRepository.findById(TEST_URL)).thenReturn(Optional.of(sampleRepositoryDocument));
        Mockito.doNothing().when(gitRepositoryDocumentRepository).deleteById(TEST_URL);

        // when
        gitRepositoryPersistenceService.delete(TEST_URL);

        // then
        Mockito.verify(gitRepositoryDocumentRepository, Mockito.times(1)).deleteById(TEST_URL);
    }
} 