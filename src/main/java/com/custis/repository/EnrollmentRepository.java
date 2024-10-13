package com.custis.repository;

import com.custis.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    Boolean existsByCourseIdAndStudentId(Long courseId, Long studentId);
}
