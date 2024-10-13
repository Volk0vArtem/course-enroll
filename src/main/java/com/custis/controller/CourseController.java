package com.custis.controller;

import com.custis.dto.CourseDto;
import com.custis.service.CourseService;
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
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService service;

    @PostMapping
    public ResponseEntity<CourseDto> addCourse(@RequestBody @Valid CourseDto courseDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addCourse(courseDto));
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<CourseDto> getCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok().body(service.getCourseById(courseId));
    }

    @GetMapping
    public ResponseEntity<List<CourseDto>> getAllCourses() {
        return ResponseEntity.ok().body(service.getAllCourses());
    }

    @GetMapping("/available")
    public ResponseEntity<List<CourseDto>> getAvailableCourses() {
        return ResponseEntity.ok().body(service.getAvailableCourses());
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long courseId) {
        service.deleteCourse(courseId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{courseId}")
    public ResponseEntity<CourseDto> patchCourse(@RequestBody @Valid CourseDto patchedCourse,
                                                 @PathVariable Long courseId) {
        return ResponseEntity.ok().body(service.patchCourse(patchedCourse, courseId));
    }
}