package com.mcewen.students.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {

    @Bean
    public RouterFunction<ServerResponse> routerFunction(StudentHandler handler) {
        return route(POST("/students"), handler::createStudent)
                .andRoute(GET("/students/active"), handler::getActiveStudents)

        .andRoute(GET("/debug/db"), handler::debugDb);
    }
}
