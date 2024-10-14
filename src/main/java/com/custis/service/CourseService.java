package com.custis.service;

import com.custis.dto.CourseDto;
import com.custis.dto.mapper.CourseMapper;
import com.custis.exception.NotFoundException;
import com.custis.model.Course;
import com.custis.model.Enrollment;
import com.custis.model.Student;
import com.custis.repository.CourseRepository;
import com.custis.repository.EnrollmentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseMapper courseMapper;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Transactional
    public CourseDto addCourse(CourseDto courseDto) {
        if (courseDto.getIsAvailable() == null) courseDto.setIsAvailable(true);
        return courseMapper.toCourseDto(courseRepository.save(courseMapper.toCourse(courseDto)));
    }

    public CourseDto getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Course with id=" + id + "not found"));
        course.setStudents(getStudentsForCourse(course.getId()));
        return courseMapper.toCourseDto(course);
    }

    public List<CourseDto> getAllCourses() {
        return courseRepository.findAll().stream()
                .peek(c -> c.setStudents(getStudentsForCourse(c.getId())))
                .map(courseMapper::toCourseDto)
                .collect(Collectors.toList());
    }

    public List<CourseDto> getAvailableCourses() {
        return courseRepository.findAllByIsAvailable(true).stream()
                .peek(c -> c.setStudents(getStudentsForCourse(c.getId())))
                .map(courseMapper::toCourseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    @Transactional
    public CourseDto patchCourse(CourseDto courseDto, Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Course with id " + id + "not found."));
        Course patchedCourse = courseMapper.toCourse(courseDto);

        if (patchedCourse.getName() != null) {
            course.setName(patchedCourse.getName());
        }
        if (patchedCourse.getLimit() != null) {
            course.setLimit(patchedCourse.getLimit());
        }
        if (patchedCourse.getStart() != null) {
            course.setStart(patchedCourse.getStart());
        }
        if (patchedCourse.getEnd() != null) {
            course.setEnd(patchedCourse.getEnd());
        }
        if (course.getLimit() < course.getStudents().size()) {
            course.setIsAvailable(false);
        }

        return courseMapper.toCourseDto(courseRepository.save(course));
    }

    public Set<Student> getStudentsForCourse(Long courseId) {
        return enrollmentRepository.findAllByCourseId(courseId).stream()
                .map(Enrollment::getStudent)
                .collect(Collectors.toSet());
    }

}