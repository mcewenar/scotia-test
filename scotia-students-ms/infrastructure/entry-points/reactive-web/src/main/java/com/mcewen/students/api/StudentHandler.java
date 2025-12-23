package com.mcewen.students.api;

import com.mcewen.students.api.dto.CreateStudentRequest;

import com.mcewen.students.api.dto.StudentMapper;
import com.mcewen.students.api.error.BadRequestResponseException;
import com.mcewen.students.usecase.CreateStudentUseCase;
import com.mcewen.students.usecase.GetActiveStudentsUseCase;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StudentHandler {

    private final DatabaseClient db;

    private final CreateStudentUseCase createStudentUseCase;
    private final GetActiveStudentsUseCase getActiveStudentsUseCase;

    private final Validator validator;


    public Mono<ServerResponse> createStudent(ServerRequest request) {
        return request.bodyToMono(CreateStudentRequest.class)
                .flatMap(this::validateOrFail)
                .map(StudentMapper::toDomain)
                .flatMap(createStudentUseCase::execute)     // MUST return Mono<Student>
                .flatMap(saved ->
                        ServerResponse.status(201).contentType(MediaType.APPLICATION_JSON).bodyValue(saved)
                );
    }


    public Mono<ServerResponse> getActiveStudents(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(getActiveStudentsUseCase.execute().map(StudentMapper::toResponse),Object.class);
    }

    private Mono<CreateStudentRequest> validateOrFail(CreateStudentRequest dto) {
        Set<ConstraintViolation<CreateStudentRequest>> violations = validator.validate(dto);
        if (violations.isEmpty()) {
            return Mono.just(dto);
        }

        Map<String, String> errors = violations.stream()
                .collect(Collectors.toMap(
                        v -> v.getPropertyPath().toString(),
                        ConstraintViolation::getMessage,
                        (a, b) -> a
                ));

        return ServerResponse.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of(
                        "message", "Validation failed",
                        "errors", errors
                ))
                .flatMap(resp -> Mono.error(new BadRequestResponseException(resp)));
    }




    public Mono<ServerResponse> debugDb(ServerRequest request) {
        Mono<Map<String, String>> dbInfo =
                db.sql("select current_database() as db, current_schema() as schema")
                        .map((row, meta) -> Map.of(
                                "db", row.get("db", String.class),
                                "schema", row.get("schema", String.class)
                        ))
                        .one();

        Mono<Long> count =
                db.sql("select count(*) as c from student")
                        .map((row, meta) -> row.get("c", Long.class))
                        .one();

        return Mono.zip(dbInfo, count)
                .flatMap(tuple -> ServerResponse.ok().bodyValue(
                        Map.of("dbInfo", tuple.getT1(), "count", tuple.getT2())
                ));
    }



}
