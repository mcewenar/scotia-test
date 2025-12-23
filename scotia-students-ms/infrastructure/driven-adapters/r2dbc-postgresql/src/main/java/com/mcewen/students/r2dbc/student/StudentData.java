package com.mcewen.students.r2dbc.student;

import com.mcewen.students.model.StudentStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("student")
public class StudentData {

    @Id
    private String id;

    @Column("first_name")
    private String firstName;

    @Column("last_name")
    private String lastName;

    private Integer age;

    private StudentStatus status;


}
