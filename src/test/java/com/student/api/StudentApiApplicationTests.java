
package com.student.api;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentApiApplicationTests {

    @Test
    void testAddStudent() {
        StudentRepository repository = mock(StudentRepository.class);
        StudentController controller = new StudentController(repository);

        when(repository.save(any(Student.class)))
            .thenAnswer(i -> i.getArgument(0));

        Student result = controller.addStudent(
            new StudentRequest("Kamal", "kamal@gmail.com")
        );

        assertEquals("Kamal", result.getName());
        verify(repository).save(any(Student.class));
    }

    @Test
    void testGetStudents() {
        StudentRepository repository = mock(StudentRepository.class);
        StudentController controller = new StudentController(repository);

        Student student = new Student();
        student.setName("Kamal");

        when(repository.findAll()).thenReturn(List.of(student));

        assertEquals("Kamal", controller.getStudents().get(0).getName());
    }
}
