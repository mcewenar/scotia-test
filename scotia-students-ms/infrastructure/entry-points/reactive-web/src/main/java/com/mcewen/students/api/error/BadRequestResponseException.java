package com.mcewen.students.api.error;

import lombok.Getter;
import org.springframework.web.reactive.function.server.ServerResponse;

@Getter
public class BadRequestResponseException extends RuntimeException {
    private final transient ServerResponse response;

    public BadRequestResponseException(ServerResponse response) {
        this.response = response;
    }

}
