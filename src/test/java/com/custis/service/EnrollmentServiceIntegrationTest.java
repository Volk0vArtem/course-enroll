package com.custis.service;

import com.custis.dto.EnrollmentDto;
import com.custis.exception.BadRequestException;
import com.custis.exception.NotFoundException;
import com.custis.model.Course;
import com.custis.model.Student;
import com.custis.repository.CourseRepository;
import com.custis.repository.EnrollmentRepository;
import com.custis.repository.StudentRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.ZonedDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class EnrollmentServiceIntegrationTest {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @BeforeEach
    void setUp() {
        enrollmentRepository.deleteAll();
        courseRepository.deleteAll();
        studentRepository.deleteAll();
    }

    @Test
    void testAddEnrollment() {
        Course course = new Course();
        course.setName("Java Basics");
        course.setLimit(30);
        course.setIsAvailable(true);
        course.setStart(ZonedDateTime.now().minusDays(1));
        course.setEnd(ZonedDateTime.now().plusDays(1));
        courseRepository.save(course);

        Student student = new Student();
        student.setName("Student1");
        studentRepository.save(student);

        EnrollmentDto enrollmentDto = enrollmentService.addEnrollment(course.getId(), student.getId());

        assertThat(enrollmentDto).isNotNull();
        assertThat(enrollmentDto.getStudent().getId()).isEqualTo(student.getId());
        assertThat(enrollmentDto.getCourse().getId()).isEqualTo(course.getId());
    }

    @Test
    void testAddEnrollmentStudentNotFound() {
        Course course = new Course();
        course.setName("Java Basics");
        course.setLimit(30);
        course.setIsAvailable(true);
        course.setStart(ZonedDateTime.now().minusDays(1));
        course.setEnd(ZonedDateTime.now().plusDays(1));
        courseRepository.save(course);

        assertThatThrownBy(() -> enrollmentService.addEnrollment(course.getId(), 999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Student with id=999 not found");
    }

    @Test
    void testAddEnrollmentCourseNotFound() {
        Student student = new Student();
        student.setName("Student1");
        studentRepository.save(student);

        assertThatThrownBy(() -> enrollmentService.addEnrollment(999L, student.getId()))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Course with id=999 not found");
    }

    @Test
    void testAddEnrollmentCourseNotAvailable() {
        Course course = new Course();
        course.setName("Java Basics");
        course.setLimit(30);
        course.setIsAvailable(false);
        course.setStart(ZonedDateTime.now().minusDays(1));
        course.setEnd(ZonedDateTime.now().plusDays(1));
        courseRepository.save(course);

        Student student = new Student();
        student.setName("Student1");
        studentRepository.save(student);

        assertThatThrownBy(() -> enrollmentService.addEnrollment(course.getId(), student.getId()))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Course is full");
    }

    @Test
    void testAddEnrollmentAlreadyEnrolled() {
        Course course = new Course();
        course.setName("Java Basics");
        course.setLimit(30);
        course.setIsAvailable(true);
        course.setStart(ZonedDateTime.now().minusDays(1));
        course.setEnd(ZonedDateTime.now().plusDays(1));
        courseRepository.save(course);

        Student student = new Student();
        student.setName("Student1");
        studentRepository.save(student);

        enrollmentService.addEnrollment(course.getId(), student.getId());

        assertThatThrownBy(() -> enrollmentService.addEnrollment(course.getId(), student.getId()))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Student is already enrolled in this course");
    }

    @Test
    void testDeleteEnrollment() {
        Course course = new Course();
        course.setName("Java Basics");
        course.setLimit(30);
        course.setIsAvailable(true);
        course.setStart(ZonedDateTime.now().minusDays(1));
        course.setEnd(ZonedDateTime.now().plusDays(1));
        courseRepository.save(course);

        Student student = new Student();
        student.setName("Student1");
        studentRepository.save(student);

        EnrollmentDto enrollmentDto = enrollmentService.addEnrollment(course.getId(), student.getId());

        enrollmentService.deleteEnrollment(course.getId(), student.getId());


        assertThat(enrollmentRepository.existsById(enrollmentDto.getId())).isFalse();
    }

    @Test
    void testDeleteEnrollmentNotFound() {
        assertThatThrownBy(() -> enrollmentService.deleteEnrollment(999L, 999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Student with id=999 not found");
    }

    @Test
    void testAddEnrollmentOutsideEnrollmentPeriod() {

        Course course = new Course();
        course.setName("Java Basics");
        course.setLimit(30);
        course.setIsAvailable(true);
        course.setStart(ZonedDateTime.now().plusDays(1));
        course.setEnd(ZonedDateTime.now().plusDays(2));
        courseRepository.save(course);

        Student student = new Student();
        student.setName("Student1");
        studentRepository.save(student);

        assertThatThrownBy(() -> enrollmentService.addEnrollment(course.getId(), student.getId()))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Course is not available for enrollment at this time.");
    }
}