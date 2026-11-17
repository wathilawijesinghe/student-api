
package com.student.api;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentRepository repository;

    public StudentController(StudentRepository repository) {
        this.repository = repository;
    }

    // CREATE - Add a student
    @PostMapping
    public Student addStudent(@RequestBody Student student) {
        student.setId(null);
        return repository.save(student);
    }

    // READ - Get all students
    @GetMapping
    public List<Student> getStudents() {
        return repository.findAll();
    }

    // READ - Get one student
    @GetMapping("/{id}")
    public Student getStudent(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND));
    }

    // UPDATE - Update a student
    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable Long id,
                                 @RequestBody Student details) {
        Student student = getStudent(id);
        student.setName(details.getName());
        student.setEmail(details.getEmail());
        return repository.save(student);
    }

    // DELETE - Delete a student
    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id) {
        Student student = getStudent(id);
        repository.delete(student);
    }
}
