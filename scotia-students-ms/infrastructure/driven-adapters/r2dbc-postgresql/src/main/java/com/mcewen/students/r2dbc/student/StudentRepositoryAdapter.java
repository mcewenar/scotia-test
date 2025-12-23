package com.mcewen.students.r2dbc.student;

import com.mcewen.students.model.Student;
import com.mcewen.students.model.StudentStatus;
import com.mcewen.students.model.gateways.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@Slf4j
@RequiredArgsConstructor
public class StudentRepositoryAdapter implements StudentRepository {

    private final StudentDataRepository repo;
    private final DatabaseClient db;

    @Override
    public Mono<Boolean> existsById(String id) {
        return repo.existsById(id);
    }

    @Override
    public Mono<Void> save(Student student) {
        return db.sql("""
                INSERT INTO student (id, first_name, last_name, age, status)
                VALUES (:id, :firstName, :lastName, :age, :status)
                """)
                .bind("id",student.getId())
                .bind("firstName", student.getFirstName())
                .bind("lastName", student.getLastName())
                .bind("age", student.getAge())
                .bind("status", student.getStatus().name())
                .fetch()
                .rowsUpdated()
                .flatMap(rows -> rows == 1
                                ? Mono.empty()
                        : Mono.error(new IllegalStateException("Insert failed"))
                )
                .doOnSuccess(v -> log.info("Inserted student id={}", student.getId()))
                .doOnError(e -> log.error("DB insert error")).then();
    }
/*    @Override
    public Mono<Void> save(Student student) {
        return repo.save(toData(student))
                //.doOnSubscribe(s -> log.info("Saving student id={}", student.getId()))
                .doOnSuccess(d -> log.info("Saved student with id={}", d.getId()))
                .doOnError(e -> log.error("DB error", e)).then();
                //.map(this::toDomain);
    }

 */

    @Override
    public Flux<Student> findAllByStatus(StudentStatus status) {
        return repo.findAllByStatus(status).map(this::toDomain);
    }

    private StudentData toData(Student student) {
        return StudentData.builder()
                .id(student.getId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .age(student.getAge())
                .status(student.getStatus())
                .build();
    }

    private Student toDomain(StudentData data) {
        return Student.builder()
                .id(data.getId())
                .firstName(data.getFirstName())
                .lastName(data.getLastName())
                .age(data.getAge())
                .status(data.getStatus())
                .build();
    }

}

