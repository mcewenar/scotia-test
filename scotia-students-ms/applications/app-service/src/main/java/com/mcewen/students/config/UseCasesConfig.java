package com.mcewen.students.config;

import com.mcewen.students.model.gateways.StudentRepository;
import com.mcewen.students.usecase.CreateStudentUseCase;
import com.mcewen.students.usecase.GetActiveStudentsUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(basePackages = "com.mcewen.students.usecase",
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "^.+UseCase$")
        },
        useDefaultFilters = false)
public class UseCasesConfig {

    @Bean
    public CreateStudentUseCase createStudentUseCase(StudentRepository repository) {
        return new CreateStudentUseCase(repository);
    }

    @Bean
    public GetActiveStudentsUseCase getActiveStudentsUseCase(StudentRepository repository) {
        return new GetActiveStudentsUseCase(repository);
    }
}
