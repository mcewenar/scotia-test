package com.mcewen.students.r2dbc;

import com.mcewen.students.model.StudentStatus;
import com.mcewen.students.r2dbc.student.StudentData;
import com.mcewen.students.r2dbc.student.StudentDataRepository;
import com.mcewen.students.r2dbc.student.StudentRepositoryAdapter;
import org.junit.jupiter.api.Test;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class StudentRepositoryAdapterUnitTest {

    @Test
    void existsByIdShouldDelegateToRepo() {
        StudentDataRepository repo = mock(StudentDataRepository.class);
        DatabaseClient db = mock(DatabaseClient.class);

        when(repo.existsById("1")).thenReturn(Mono.just(true));

        StudentRepositoryAdapter adapter = new StudentRepositoryAdapter(repo, db);

        StepVerifier.create(adapter.existsById("1"))
                .expectNext(true)
                .verifyComplete();

        verify(repo).existsById("1");
        verifyNoMoreInteractions(repo);
    }

    @Test
    void findAllByStatusShouldMapToDomain() {
        StudentDataRepository repo = mock(StudentDataRepository.class);
        DatabaseClient db = mock(DatabaseClient.class);
        StudentData d1 = StudentData.builder()
                .id("1")
                .firstName("Ana")
                .lastName("Gomez")
                .age(21)
                .status(StudentStatus.ACTIVE)
                .build();

        when(repo.findAllByStatus(StudentStatus.ACTIVE)).thenReturn(Flux.just(d1));

        StudentRepositoryAdapter adapter = new StudentRepositoryAdapter(repo, db);

        StepVerifier.create(adapter.findAllByStatus(StudentStatus.ACTIVE))
                .assertNext(s -> {
                    org.assertj.core.api.Assertions.assertThat(s.getId()).isEqualTo("1");
                    org.assertj.core.api.Assertions.assertThat(s.getFirstName()).isEqualTo("Ana");
                    org.assertj.core.api.Assertions.assertThat(s.getLastName()).isEqualTo("Gomez");
                    org.assertj.core.api.Assertions.assertThat(s.getAge()).isEqualTo(21);
                    org.assertj.core.api.Assertions.assertThat(s.getStatus()).isEqualTo(StudentStatus.ACTIVE);
                })
                .verifyComplete();

        verify(repo).findAllByStatus(StudentStatus.ACTIVE);
        verifyNoMoreInteractions(repo);
    }
}
