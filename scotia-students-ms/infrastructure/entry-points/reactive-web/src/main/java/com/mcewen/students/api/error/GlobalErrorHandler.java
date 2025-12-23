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
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        pd.setTitle("Duplicate student id");
        pd.setDetail(ex.getMessage());
        return Mono.just(pd);
    }

    @ExceptionHandler(BadRequestResponseException.class)
    public Mono<ServerResponse> handleBadRequestPrepared(BadRequestResponseException ex) {
        return Mono.just(ex.getResponse());
    }
}
