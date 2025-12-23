package com.mcewen.students.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Student {
    String id;
    String firstName;
    String lastName;
    Integer age;
    StudentStatus status;
}
