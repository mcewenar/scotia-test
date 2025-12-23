package com.mcewen.students.usecase;

import com.mcewen.students.model.Student;
import com.mcewen.students.model.StudentStatus;
import com.mcewen.students.model.gateways.StudentRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class GetActiveStudentsUseCase {

    private final StudentRepository repository;

    public Flux<Student> execute() {
        return repository.findAllByStatus(StudentStatus.ACTIVE);
    }
}
