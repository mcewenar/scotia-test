package com.mcewen.students.api;

import com.mcewen.students.api.dto.CreateStudentRequest;
import com.mcewen.students.api.dto.StudentResponse;
import com.mcewen.students.model.Student;
import com.mcewen.students.model.StudentStatus;
import com.mcewen.students.usecase.CreateStudentUseCase;
import com.mcewen.students.usecase.GetActiveStudentsUseCase;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class StudentHandlerTest {

    private CreateStudentUseCase createStudentUseCase;
    private GetActiveStudentsUseCase getActiveStudentsUseCase;
    private Validator validator;

    private WebTestClient webTestClient;

    @BeforeEach
    void setup() {
        createStudentUseCase = Mockito.mock(CreateStudentUseCase.class);
        getActiveStudentsUseCase = Mockito.mock(GetActiveStudentsUseCase.class);
        validator = Mockito.mock(Validator.class);

        StudentHandler handler =
                new StudentHandler(createStudentUseCase, getActiveStudentsUseCase, validator);

        RouterFunction<ServerResponse> router = new RouterRest().routerFunction(handler);

        webTestClient = WebTestClient.bindToRouterFunction(router).build();
    }


    @Test
    void shouldCreateStudentWhenRequestIsValid() {
        when(validator.validate(any(CreateStudentRequest.class)))
                .thenReturn(Collections.emptySet());

        when(createStudentUseCase.execute(any(Student.class)))
                .thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                          "firstName": "Ana",
                          "lastName": "Gomez",
                          "age": 21,
                          "status": "ACTIVE"
                        }
                        """)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo("1");

        verify(createStudentUseCase, times(1)).execute(any(Student.class));
    }

    @Test
    void shouldReturnBadRequestWhenValidationFails() {
        @SuppressWarnings("unchecked")
        ConstraintViolation<CreateStudentRequest> violation =
                (ConstraintViolation<CreateStudentRequest>) mock(ConstraintViolation.class);

        when(violation.getMessage()).thenReturn("must not be blank");

        when(validator.validate(any(CreateStudentRequest.class)))
                .thenReturn(Set.of(violation));

        webTestClient.post()
                .uri("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                          "firstName": "",
                          "lastName": "Gomez",
                          "age": 21,
                          "status": "ACTIVE"
                        }
                        """)
                .exchange()
                .expectStatus().isBadRequest();

        verify(createStudentUseCase, never()).execute(any());
    }


    @Test
    void shouldGetActiveStudents() {
        when(getActiveStudentsUseCase.execute())
                .thenReturn(Flux.just(
                        Student.builder()
                                .id("1")
                                .firstName("Ana")
                                .lastName("Gomez")
                                .age(21)
                                .status(StudentStatus.ACTIVE)
                                .build()
                ));

        webTestClient.get()
                .uri("/students/active")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody();

        verify(getActiveStudentsUseCase, times(1)).execute();
    }
}

