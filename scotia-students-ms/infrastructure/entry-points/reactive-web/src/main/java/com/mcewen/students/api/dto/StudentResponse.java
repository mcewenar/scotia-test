package com.mcewen.students.api.dto;

import com.mcewen.students.model.StudentStatus;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class StudentResponse {
    String id;
    String firstName;
    String lastName;
    Integer age;
    StudentStatus status;
}
