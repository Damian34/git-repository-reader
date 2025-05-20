package com.damian34.gitreader.infrastructure.service.persistence;

import com.damian34.gitreader.TestContainerInitializer;
import com.damian34.gitreader.domain.dto.GitRepositoryDto;
import com.damian34.gitreader.exception.NotFoundGitRepositoryException;
import com.damian34.gitreader.infrastructure.db.GitRepositoryDocumentRepository;
import com.damian34.gitreader.model.ProcessStatus;
import com.damian34.gitreader.model.document.GitRepositoryDocument;
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
        GitRepositoryDto result = gitRepositoryPersistenceService.findGitRepository(TEST_URL);

        // then
        Assertions.assertEquals(TEST_URL, result.getUrl());
        Assertions.assertEquals(1, result.getBranches().size());
        Assertions.assertEquals("main", result.getBranches().get(0).getName());
    }

    @Test
    void shouldThrowExceptionWhenRepositoryNotFound() {
        // given
        Mockito.when(gitRepositoryDocumentRepository.findById(TEST_URL)).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThrows(NotFoundGitRepositoryException.class, 
                () -> gitRepositoryPersistenceService.findGitRepository(TEST_URL));
    }

    @Test
    void shouldReturnRepositoriesWhenDataExists() {
        // given
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
        Mockito.when(gitRepositoryDocumentRepository.findAll()).thenReturn(Collections.emptyList());

        // when
        List<GitRepositoryDto> results = gitRepositoryPersistenceService.findAllGitRepositories();

        // then
        Assertions.assertTrue(results.isEmpty());
    }

    @ParameterizedTest
    @EnumSource(value = ProcessStatus.class, names = {"COMPLETED", "FAILED"})
    void shouldCallDeleteByUrlAndNotStatusWhenStatusNotMatching(ProcessStatus status) {
        // given
        sampleRepositoryDocument.setStatus(status);
        Mockito.when(gitRepositoryDocumentRepository.findById(TEST_URL)).thenReturn(Optional.of(sampleRepositoryDocument));
        
        // when
        gitRepositoryPersistenceService.cleanRepositories(TEST_URL, ProcessStatus.WAITING);

        // then
        Mockito.verify(gitRepositoryDocumentRepository).deleteByUrlAndNotStatus(TEST_URL, ProcessStatus.WAITING);
    }
} 