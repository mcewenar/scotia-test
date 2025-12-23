package com.mcewen.students.api.error;

import com.mcewen.students.model.exceptions.DuplicateStudentIdException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(DuplicateStudentIdException.class)
    public Mono<ProblemDetail> handleDuplicate(DuplicateStudentIdException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problemDetail.setTitle("Duplicate student id");
        problemDetail.setDetail(ex.getMessage());
        return Mono.just(problemDetail);
    }

    @ExceptionHandler(BadRequestResponseException.class)
    public Mono<ServerResponse> handleBadRequestPrepared(BadRequestResponseException ex) {
        return Mono.just(ex.getResponse());
    }
}
