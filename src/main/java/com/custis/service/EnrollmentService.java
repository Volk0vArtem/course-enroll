package com.custis.service;

import com.custis.dto.EnrollmentDto;
import com.custis.dto.mapper.EnrollmentMapper;
import com.custis.exception.BadRequestException;
import com.custis.exception.NotFoundException;
import com.custis.model.Course;
import com.custis.model.Student;
import com.custis.repository.CourseRepository;
import com.custis.repository.EnrollmentRepository;
import com.custis.repository.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentMapper mapper;


    @Transactional
    public EnrollmentDto addEnrollment(EnrollmentDto enrollmentDto) {
        Student student = studentRepository.findById(enrollmentDto.getStudentId())
                .orElseThrow(() -> new NotFoundException("Student with id=" + enrollmentDto.getStudentId() + "not found"));
        Course course = courseRepository.findById(enrollmentDto.getCourseId())
                .orElseThrow(() -> new NotFoundException("Course with id=" + enrollmentDto.getCourseId() + "not found"));
        if (!course.getIsAvailable()) {
            throw new BadRequestException("Course is full");
        }
        if (enrollmentRepository.existsByCourseIdAndStudentId(enrollmentDto.getCourseId(), enrollmentDto.getStudentId())) {
            throw new BadRequestException("Student is already enrolled in this course");
        }
        if (course.getStudents().size() + 1 >= course.getLimit()) {
            course.setIsAvailable(false);
            courseRepository.save(course);
        }
        student.getCourses().add(course);
        studentRepository.save(student);
        course.getStudents().add(student);
        courseRepository.save(course);
        return mapper.toEnrollmentDto(enrollmentRepository.save(mapper.toEnrollment(enrollmentDto)));
    }
}
