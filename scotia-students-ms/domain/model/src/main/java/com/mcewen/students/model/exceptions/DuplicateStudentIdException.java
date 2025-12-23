package com.mcewen.students.model.exceptions;

public class DuplicateStudentIdException extends RuntimeException {
    public DuplicateStudentIdException(String id) {
        super("Student with id '" + id+ "' already exists");
    }
}
