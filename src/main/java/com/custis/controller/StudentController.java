package com.custis.controller;

import com.custis.dto.StudentDto;
import com.custis.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService service;

    @PostMapping
    public ResponseEntity<StudentDto> addStudent(@RequestBody @Valid StudentDto studentDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addStudent(studentDto));
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable Long studentId) {
        return ResponseEntity.ok().body(service.getStudentById(studentId));
    }

    @GetMapping
    public ResponseEntity<List<StudentDto>> getAllStudents() {
        return ResponseEntity.ok().body(service.getAllStudents());
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long studentId) {
        service.deleteStudent(studentId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<StudentDto> patchStudent(@RequestBody @Valid StudentDto studentDto,
                                                   @PathVariable Long id) {
        return ResponseEntity.ok().body(service.patchStudent(studentDto, id));
    }
}