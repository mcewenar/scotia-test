package com.mcewen.students.api.dto;

import com.mcewen.students.model.Student;

public final class StudentMapper {

    public static Student toDomain(CreateStudentRequest req) {
        return Student.builder()
                .id(req.getId())
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .age(req.getAge())
                .status(req.getStatus())
                .build();
    }

    public static StudentResponse toResponse(Student s) {
        return StudentResponse.builder()
                .id(s.getId())
                .firstName(s.getFirstName())
                .lastName(s.getLastName())
                .age(s.getAge())
                .status(s.getStatus())
                .build();
    }
}
