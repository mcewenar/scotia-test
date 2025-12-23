package com.mcewen.students.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {RouterRest.class, StudentHandler.class})
//@WebFluxTest
class RouterRestTest {


    private StudentHandler handler;
    private WebTestClient webTestClient;

    @BeforeEach
    void setup() {
        handler = Mockito.mock(StudentHandler.class);

        RouterRest routerRest = new RouterRest();
        RouterFunction<ServerResponse> routerFunction = routerRest.routerFunction(handler);

        webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
    }

    @Test
    void shouldRoutePostStudentsToCreateStudentHandler() {
        when(handler.createStudent(any()))
                .thenReturn(ServerResponse.status(201).build());

        webTestClient.post()
                .uri("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                          "id": "999",
                          "firstName": "Test",
                          "lastName": "Insert",
                          "age": 30,
                          "status": "ACTIVE"
                        }
                        """)
                .exchange()
                .expectStatus().isCreated();

        verify(handler, times(1)).createStudent(any());
        verify(handler, never()).getActiveStudents(any());
    }

    @Test
    void shouldRouteGetStudentsActiveToGetActiveStudentsHandler() {
        when(handler.getActiveStudents(any()))
                .thenReturn(ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue("[]"));

        webTestClient.get()
                .uri("/students/active")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON);

        verify(handler, times(1)).getActiveStudents(any());
        verify(handler, never()).createStudent(any());
    }










   /* @Autowired
    private WebTestClient webTestClient;

    @Test
    void testListenGETUseCase() {
        webTestClient.get()
                .uri("/api/usecase/path")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(userResponse -> {
                            Assertions.assertThat(userResponse).isEmpty();
                        }
                );
    }

    @Test
    void testListenGETOtherUseCase() {
        webTestClient.get()
                .uri("/api/otherusercase/path")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(userResponse -> {
                            Assertions.assertThat(userResponse).isEmpty();
                        }
                );
    }

    @Test
    void testListenPOSTUseCase() {
        webTestClient.post()
                .uri("/api/usecase/otherpath")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue("")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(userResponse -> {
                            Assertions.assertThat(userResponse).isEmpty();
                        }
                );
    }

    */
}
