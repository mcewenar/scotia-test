package com.mcewen.students.config;

import com.mcewen.students.model.gateways.StudentRepository;
import com.mcewen.students.usecase.CreateStudentUseCase;
import com.mcewen.students.usecase.GetActiveStudentsUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class UseCasesConfigTest {

    @Test
    void testUseCaseBeansExist() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class)) {
            String[] beanNames = context.getBeanDefinitionNames();

            boolean useCaseBeanFound = false;
            for (String beanName : beanNames) {
                if (beanName.endsWith("UseCase")) {
                    useCaseBeanFound = true;
                    break;
                }
            }

            assertTrue(useCaseBeanFound, "No beans ending with 'Use Case' were found");
            assertNotNull(context.getBean(CreateStudentUseCase.class));
            assertNotNull(context.getBean(GetActiveStudentsUseCase.class));

            assertTrue(context.containsBean("createStudentUseCase"));
            assertTrue(context.containsBean("getActiveStudentsUseCase"));
        }
    }

    @Configuration
    @Import(UseCasesConfig.class)
    static class TestConfig {

        @Bean
        StudentRepository studentRepository() {
            return mock(StudentRepository.class);
        }
    }

    static class MyUseCase {
        public String execute() {
            return "MyUseCase Test";
        }
    }
}