package com.mcewen.students.api.dto;

import com.mcewen.students.model.StudentStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateStudentRequest {

    @NotBlank
    private String id;
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotNull
    @Min(0)
    @Max(120)
    private Integer age;

    @NotNull
    private StudentStatus status;
}
