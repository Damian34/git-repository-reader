package com.damian34.gitreader.infrastructure.service.persistence;

import com.damian34.gitreader.TestContainerInitializer;
import com.damian34.gitreader.domain.dto.GitRepositoryDto;
import com.damian34.gitreader.exception.NotFoundGitRepositoryException;
import com.damian34.gitreader.infrastructure.db.GitRepositoryDocumentRepository;
import com.damian34.gitreader.infrastructure.db.GitStatusRepository;
import com.damian34.gitreader.model.ProcessStatus;
import com.damian34.gitreader.model.document.GitRepositoryDocument;
import com.damian34.gitreader.model.document.GitStatusDocument;
import com.damian34.gitreader.model.repository.Branch;
import com.damian34.gitreader.model.repository.Commit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@ContextConfiguration(initializers = TestContainerInitializer.class)
class GitRepositoryPersistenceServiceImplTest {

    private static final String TEST_URL = "https://github.com/Damian34/git-repository-reader.git";

    @MockitoBean
    private GitRepositoryDocumentRepository gitRepositoryDocumentRepository;

    @MockitoBean
    private GitStatusRepository gitStatusRepository;

    @Autowired
    private GitRepositoryPersistenceServiceImpl gitRepositoryPersistenceService;

    private GitStatusDocument sampleStatusDocument;
    private GitRepositoryDocument sampleRepositoryDocument;

    @BeforeEach
    void setUp() {
        sampleStatusDocument = new GitStatusDocument(TEST_URL, null, ProcessStatus.COMPLETED, null);
        
        Branch branch = new Branch("branchId", "main", 
                List.of(new Commit("commitId", "Initial commit", "Author", "2023-01-01")));
        sampleRepositoryDocument = new GitRepositoryDocument(TEST_URL, null, List.of(branch));
    }

    @Test
    void shouldReturnDtoWhenRepositoryExists() {
        // given
        Mockito.when(gitStatusRepository.findById(TEST_URL)).thenReturn(Optional.of(sampleStatusDocument));
        Mockito.when(gitRepositoryDocumentRepository.findById(TEST_URL)).thenReturn(Optional.of(sampleRepositoryDocument));

        // when
        GitRepositoryDto result = gitRepositoryPersistenceService.findGitRepository(TEST_URL);

        // then
        Assertions.assertEquals(TEST_URL, result.getUrl());
        Assertions.assertEquals(1, result.getBranches().size());
        Assertions.assertEquals("main", result.getBranches().get(0).getName());
    }

    @Test
    void shouldThrowExceptionWhenStatusNotFound() {
        // given
        Mockito.when(gitStatusRepository.findById(TEST_URL)).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThrows(NotFoundGitRepositoryException.class, 
                () -> gitRepositoryPersistenceService.findGitRepository(TEST_URL));
    }

    @Test
    void shouldReturnRepositoriesWhenDataExists() {
        // given
        Mockito.when(gitStatusRepository.findAll()).thenReturn(List.of(sampleStatusDocument));
        Mockito.when(gitRepositoryDocumentRepository.findAll()).thenReturn(List.of(sampleRepositoryDocument));

        // when
        List<GitRepositoryDto> results = gitRepositoryPersistenceService.findAllGitRepositories();

        // then
        Assertions.assertEquals(1, results.size());
        Assertions.assertEquals(TEST_URL, results.get(0).getUrl());
    }

    @Test
    void shouldReturnEmptyListWhenNoDataExists() {
        // given
        Mockito.when(gitStatusRepository.findAll()).thenReturn(Collections.emptyList());

        // when
        List<GitRepositoryDto> results = gitRepositoryPersistenceService.findAllGitRepositories();

        // then
        Assertions.assertTrue(results.isEmpty());
    }

    @ParameterizedTest
    @EnumSource(value = ProcessStatus.class, names = {"COMPLETED", "FAILED"})
    void shouldDeleteRepositoryWhenStatusNotMatching(ProcessStatus status) {
        // given
        sampleStatusDocument.setStatus(status);
        Mockito.when(gitStatusRepository.findById(TEST_URL)).thenReturn(Optional.of(sampleStatusDocument));
        
        // when
        gitRepositoryPersistenceService.cleanRepositories(TEST_URL, ProcessStatus.WAITING);

        // then
        Mockito.verify(gitRepositoryDocumentRepository).deleteById(TEST_URL);
        Mockito.verify(gitStatusRepository).deleteById(TEST_URL);
    }

    @Test
    void shouldNotDeleteRepositoryWhenStatusMatching() {
        // given
        sampleStatusDocument.setStatus(ProcessStatus.WAITING);
        Mockito.when(gitStatusRepository.findById(TEST_URL)).thenReturn(Optional.of(sampleStatusDocument));
        
        // when
        gitRepositoryPersistenceService.cleanRepositories(TEST_URL, ProcessStatus.WAITING);

        // then
        Mockito.verify(gitRepositoryDocumentRepository, Mockito.never()).deleteById(TEST_URL);
    }

    @Test
    void shouldNotDeleteRepositoryWhenStatusNotFound() {
        // given
        Mockito.when(gitStatusRepository.findById(TEST_URL)).thenReturn(Optional.empty());
        
        // when
        gitRepositoryPersistenceService.cleanRepositories(TEST_URL, ProcessStatus.WAITING);

        // then
        Mockito.verify(gitRepositoryDocumentRepository, Mockito.never()).deleteById(TEST_URL);
    }
} 