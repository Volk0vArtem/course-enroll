package com.custis.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnrollmentDto {
    private Long id;
    private StudentDto student;
    private CourseDto course;
}
