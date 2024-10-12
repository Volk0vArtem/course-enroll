package com.custis.repository;

import com.custis.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findAllByIsAvailable(Boolean isAvailable);
}
