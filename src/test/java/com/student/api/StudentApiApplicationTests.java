
package com.student.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class StudentApiApplicationTests {

    private StudentRepository repository;
    private StudentController controller;

    @BeforeEach
    void setUp() {
        repository = mock(StudentRepository.class);
        controller = new StudentController(repository);
    }

    @Test
    void testAddStudent() {
        StudentRequest request =
                new StudentRequest("Wathila", "wathila@gmail.com");

        when(repository.save(any(Student.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Student result = controller.addStudent(request);

        assertEquals("Wathila", result.getName());
        assertEquals("wathila@gmail.com", result.getEmail());

        verify(repository).save(any(Student.class));
    }

    @Test
    void testGetStudents() {
        Student student = new Student();
        student.setName("Wathila");
        student.setEmail("wathila@gmail.com");

        when(repository.findAll()).thenReturn(List.of(student));

        List<Student> result = controller.getStudents();

        assertEquals(1, result.size());
        assertEquals("Wathila", result.get(0).getName());

        verify(repository).findAll();
    }

    @Test
    void testGetStudentById() {
        Student student = new Student();
        student.setName("Wathila");

        when(repository.findById(1L))
                .thenReturn(Optional.of(student));

        Student result = controller.getStudent(1L);

        assertEquals("Wathila", result.getName());
    }

    @Test
    void testGetStudentNotFound() {
        when(repository.findById(99L))
                .thenReturn(Optional.empty());

        ResponseStatusException error = assertThrows(
                ResponseStatusException.class,
                () -> controller.getStudent(99L)
        );

        assertEquals(HttpStatus.NOT_FOUND, error.getStatusCode());
    }

    @Test
    void testUpdateStudent() {
        Student student = new Student();
        student.setName("Old Name");
        student.setEmail("old@gmail.com");

        when(repository.findById(1L))
                .thenReturn(Optional.of(student));

        when(repository.save(any(Student.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        StudentRequest request =
                new StudentRequest("New Name", "new@gmail.com");

        Student result = controller.updateStudent(1L, request);

        assertEquals("New Name", result.getName());
        assertEquals("new@gmail.com", result.getEmail());

        verify(repository).save(student);
    }

    @Test
    void testUpdateStudentNotFound() {
        when(repository.findById(99L))
                .thenReturn(Optional.empty());

        StudentRequest request =
                new StudentRequest("New Name", "new@gmail.com");

        ResponseStatusException error = assertThrows(
                ResponseStatusException.class,
                () -> controller.updateStudent(99L, request)
        );

        assertEquals(HttpStatus.NOT_FOUND, error.getStatusCode());
        verify(repository, never()).save(any(Student.class));
    }

    @Test
    void testDeleteStudent() {
        Student student = new Student();
        student.setName("Wathila");

        when(repository.findById(1L))
                .thenReturn(Optional.of(student));

        controller.deleteStudent(1L);

        verify(repository).delete(student);
    }

    @Test
    void testDeleteStudentNotFound() {
        when(repository.findById(99L))
                .thenReturn(Optional.empty());

        ResponseStatusException error = assertThrows(
                ResponseStatusException.class,
                () -> controller.deleteStudent(99L)
        );

        assertEquals(HttpStatus.NOT_FOUND, error.getStatusCode());
        verify(repository, never()).delete(any(Student.class));
    }

    @Test
    void testStudentNameAndEmail() {
        Student student = new Student();

        student.setName("Test Student");
        student.setEmail("test@gmail.com");

        assertEquals("Test Student", student.getName());
        assertEquals("test@gmail.com", student.getEmail());
    }
}
