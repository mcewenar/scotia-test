package com.mcewen.students.usecase;

import com.mcewen.students.model.Student;
import com.mcewen.students.model.StudentStatus;
import com.mcewen.students.model.exceptions.DuplicateStudentIdException;
import com.mcewen.students.model.gateways.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateStudentUseCaseTest {

    private StudentRepository repository;
    private CreateStudentUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(StudentRepository.class);
        useCase = new CreateStudentUseCase(repository);
    }

    @Test
    void shouldFailWhenIdIsNull() {
        Student student = Student.builder()
                .id(null)
                .firstName("Ana")
                .lastName("Gomez")
                .age(21)
                .status(StudentStatus.ACTIVE)
                .build();

        StepVerifier.create(useCase.execute(student))
                .expectErrorMatches(e ->
                        e instanceof IllegalArgumentException &&
                                "id is required".equals(e.getMessage()))
                .verify();

        verifyNoInteractions(repository);
    }

    @Test
    void shouldFailWhenIdIsBlank() {
        Student student = Student.builder()
                .id("   ")
                .firstName("Ana")
                .lastName("Gomez")
                .age(21)
                .status(StudentStatus.ACTIVE)
                .build();

        StepVerifier.create(useCase.execute(student))
                .expectErrorMatches(e ->
                        e instanceof IllegalArgumentException &&
                                "id is required".equals(e.getMessage()))
                .verify();

        verifyNoInteractions(repository);
    }

    @Test
    void shouldFailWhenStudentAlreadyExists() {
        Student student = Student.builder()
                .id("999")
                .firstName("Ana")
                .lastName("Gomez")
                .age(21)
                .status(StudentStatus.ACTIVE)
                .build();

        when(repository.existsById("999")).thenReturn(Mono.just(true));

        StepVerifier.create(useCase.execute(student))
                .expectError(DuplicateStudentIdException.class)
                .verify();

        verify(repository, times(1)).existsById("999");
        verify(repository, never()).save(any());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldSaveWhenStudentDoesNotExist() {
        Student student = Student.builder()
                .id("999")
                .firstName("Ana")
                .lastName("Gomez")
                .age(21)
                .status(StudentStatus.ACTIVE)
                .build();

        when(repository.existsById("999")).thenReturn(Mono.just(false));
        when(repository.save(student)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute(student))
                .verifyComplete();

        verify(repository, times(1)).existsById("999");
        verify(repository, times(1)).save(student);
        verifyNoMoreInteractions(repository);
    }
}

