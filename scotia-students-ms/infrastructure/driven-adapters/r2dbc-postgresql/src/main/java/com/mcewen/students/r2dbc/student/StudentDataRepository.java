package com.mcewen.students.r2dbc.student;

import com.mcewen.students.model.StudentStatus;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface StudentDataRepository extends ReactiveCrudRepository<StudentData, String>, ReactiveQueryByExampleExecutor<StudentData> {

    Flux<StudentData> findAllByStatus(StudentStatus status);
}

