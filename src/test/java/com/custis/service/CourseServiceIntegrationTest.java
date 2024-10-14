package com.custis.service;

import com.custis.dto.CourseDto;
import com.custis.exception.NotFoundException;
import com.custis.model.Course;
import com.custis.repository.CourseRepository;
import com.custis.repository.EnrollmentRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.ZonedDateTime;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CourseServiceIntegrationTest {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @BeforeEach
    void setUp() {
        enrollmentRepository.deleteAll();
        courseRepository.deleteAll();
    }

    @Test
    void testAddCourse() {
        CourseDto courseDto = new CourseDto();
        courseDto.setName("Java Basics");
        courseDto.setLimit(30);
        courseDto.setStart(ZonedDateTime.now());
        courseDto.setEnd(ZonedDateTime.now().plusDays(30));

        CourseDto savedCourse = courseService.addCourse(courseDto);

        assertThat(savedCourse).isNotNull();
        assertThat(savedCourse.getId()).isNotNull();
        assertThat(savedCourse.getName()).isEqualTo("Java Basics");
        assertThat(savedCourse.getIsAvailable()).isTrue();
    }

    @Test
    void testGetCourseById() {

        Course course = new Course();
        course.setName("Java Basics");
        course.setLimit(30);
        course.setIsAvailable(true);
        courseRepository.save(course);

        CourseDto retrievedCourse = courseService.getCourseById(course.getId());

        assertThat(retrievedCourse).isNotNull();
        assertThat(retrievedCourse.getId()).isEqualTo(course.getId());
        assertThat(retrievedCourse.getName()).isEqualTo(course.getName());
    }

    @Test
    void testGetCourseByIdNotFound() {
        assertThrows(NotFoundException.class, () -> courseService.getCourseById(999L));
    }

    @Test
    void testGetAllCourses() {
        Course course1 = new Course();
        course1.setName("Java Basics");
        course1.setLimit(30);
        course1.setIsAvailable(true);
        courseRepository.save(course1);

        Course course2 = new Course();
        course2.setName("Advanced Java");
        course2.setLimit(20);
        course2.setIsAvailable(true);
        courseRepository.save(course2);

        List<CourseDto> courses = courseService.getAllCourses();

        assertEquals(2, courses.size());
        assertThat(courses.get(0).getName()).isEqualTo("Java Basics");
        assertThat(courses.get(1).getName()).isEqualTo("Advanced Java");
    }

    @Test
    void testGetAvailableCourses() {

        Course course1 = new Course();
        course1.setName("Java Basics");
        course1.setLimit(30);
        course1.setIsAvailable(true);
        courseRepository.save(course1);

        Course course2 = new Course();
        course2.setName("Advanced Java");
        course2.setLimit(20);
        course2.setIsAvailable(false);
        courseRepository.save(course2);

        List<CourseDto> availableCourses = courseService.getAvailableCourses();

        assertEquals(1, availableCourses.size());
        assertThat(availableCourses.getFirst().getName()).isEqualTo("Java Basics");
    }

    @Test
    void testDeleteCourse() {

        Course course = new Course();
        course.setName("Java Basics");
        course.setLimit(30);
        course.setIsAvailable(true);
        courseRepository.save(course);

        courseService.deleteCourse(course.getId());

        assertThat(courseRepository.findById(course.getId())).isEmpty();
    }

    @Test
    void testPatchCourse() {

        Course course = new Course();
        course.setName("Java Basics");
        course.setLimit(30);
        course.setIsAvailable(true);
        courseRepository.save(course);

        CourseDto patchDto = new CourseDto();
        patchDto.setName("Java Advanced");

        CourseDto patchedCourse = courseService.patchCourse(patchDto, course.getId());

        assertThat(patchedCourse.getName()).isEqualTo("Java Advanced");
    }
}
