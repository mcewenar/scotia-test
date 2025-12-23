package com.mcewen.students.usecase;

import com.mcewen.students.model.Student;
import com.mcewen.students.model.exceptions.DuplicateStudentIdException;
import com.mcewen.students.model.gateways.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
@Log
public class CreateStudentUseCase {

    private final StudentRepository repository;

    public Mono<Void> execute(Student student) {
        if (student.getId() == null || student.getId().isBlank()) {
            return Mono.error(new IllegalArgumentException("id is required"));
        }
        return repository.existsById(student.getId())
                .flatMap(exists -> exists
                        ? Mono.error(new DuplicateStudentIdException(student.getId()))
                        : repository.save(student)
                );
    }
}

