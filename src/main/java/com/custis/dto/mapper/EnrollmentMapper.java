package com.custis.dto.mapper;

import com.custis.dto.EnrollmentDto;
import com.custis.model.Enrollment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EnrollmentMapper {
    Enrollment toEnrollment(EnrollmentDto enrollmentDto);

    EnrollmentDto toEnrollmentDto(Enrollment enrollment);
}
