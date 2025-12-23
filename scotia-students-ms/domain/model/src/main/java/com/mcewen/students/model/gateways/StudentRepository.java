package com.mcewen.students.model.gateways;

import com.mcewen.students.model.Student;
import com.mcewen.students.model.StudentStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StudentRepository {
    Mono<Boolean> existsById(String id);
    Mono<Void> save(Student student);
    Flux<Student> findAllByStatus(StudentStatus status);
}
