package com.custis.repository;

import com.custis.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    Enrollment findByCourseIdAndStudentId(Long courseId, Long studentId);

    Boolean existsByCourseIdAndStudentId(Long courseId, Long studentId);

    Set<Enrollment> findAllByCourseId(Long courseId);
}
