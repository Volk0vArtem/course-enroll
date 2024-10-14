package com.custis.controller;

import com.custis.dto.EnrollmentDto;
import com.custis.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService service;

    @PostMapping("/{courseId}/{studentId}")
    public ResponseEntity<EnrollmentDto> addEnrollment(@PathVariable Long courseId,
                                                       @PathVariable Long studentId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addEnrollment(courseId, studentId));
    }

    @DeleteMapping("/{courseId}/{studentId}")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable Long courseId,
                                                 @PathVariable Long studentId) {
        service.deleteEnrollment(courseId, studentId);
        return ResponseEntity.noContent().build();
    }
}