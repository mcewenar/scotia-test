package com.mcewen.students.usecase;

import com.mcewen.students.model.Student;
import com.mcewen.students.model.StudentStatus;
import com.mcewen.students.model.gateways.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class GetActiveStudentsUseCaseTest {

    private StudentRepository repository;
    private GetActiveStudentsUseCase useCase;

    @BeforeEach
    void setup() {
        repository = mock(StudentRepository.class);
        useCase = new GetActiveStudentsUseCase(repository);
    }

    @Test
    void shouldReturnActiveStudents() {
        Student s1 = Student.builder()
                .id("1")
                .firstName("Ana")
                .lastName("Gomez")
                .age(21)
                .status(StudentStatus.ACTIVE)
                .build();

        Student s2 = Student.builder()
                .id("2")
                .firstName("Juan")
                .lastName("Perez")
                .age(25)
                .status(StudentStatus.ACTIVE)
                .build();

        when(repository.findAllByStatus(StudentStatus.ACTIVE))
                .thenReturn(Flux.just(s1, s2));


        StepVerifier.create(useCase.execute())
                .expectNext(s1)
                .expectNext(s2)
                .verifyComplete();

        verify(repository, times(1)).findAllByStatus(StudentStatus.ACTIVE);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldReturnEmptyWhenNoActiveStudents() {
        when(repository.findAllByStatus(StudentStatus.ACTIVE))
                .thenReturn(Flux.empty());

        StepVerifier.create(useCase.execute())
                .verifyComplete();

        verify(repository, times(1)).findAllByStatus(StudentStatus.ACTIVE);
    }
}

