package com.custis.service;

import com.custis.dto.EnrollmentDto;
import com.custis.dto.mapper.EnrollmentMapper;
import com.custis.exception.BadRequestException;
import com.custis.exception.NotFoundException;
import com.custis.model.Course;
import com.custis.model.Enrollment;
import com.custis.model.Student;
import com.custis.repository.CourseRepository;
import com.custis.repository.EnrollmentRepository;
import com.custis.repository.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentMapper mapper;


    @Transactional
    public EnrollmentDto addEnrollment(Long courseId, Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Student with id=" + studentId + "not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course with id=" + courseId + "not found"));

        ZonedDateTime now = ZonedDateTime.now();
        if (now.isBefore(course.getStart()) || now.isAfter(course.getEnd())) {
            throw new RuntimeException("Course is not available for enrollment at this time.");
        }
        if (!course.getIsAvailable()) {
            throw new BadRequestException("Course is full");
        }
        if (enrollmentRepository.existsByCourseIdAndStudentId(courseId, studentId)) {
            throw new BadRequestException("Student is already enrolled in this course");
        }
        if (course.getStudents().size() + 1 >= course.getLimit()) {
            course.setIsAvailable(false);
            courseRepository.save(course);
        }
        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        return mapper.toEnrollmentDto(enrollmentRepository.save(enrollment));
    }

    @Transactional
    public void deleteEnrollment(Long courseId, Long studentId) {
        studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Student with id=" + studentId + "not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course with id=" + courseId + "not found"));
        Enrollment enrollment = enrollmentRepository.findByCourseIdAndStudentId(courseId, studentId);
        if (enrollment == null) {
            throw new NotFoundException("Enrollment with courseId=" + courseId + " and studentId=" + studentId + " not found");
        }
        enrollmentRepository.deleteById(enrollment.getId());
        if (!course.getIsAvailable()) {
            course.setIsAvailable(true);
            courseRepository.save(course);
        }
    }
}
