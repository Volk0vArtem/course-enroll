package com.custis.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.Set;


@Getter
@Setter
public class CourseDto {

    private Long id;

    @Size(min = 2, max = 255, message = "Name must be between 2 and 255 characters")
    private String name;

    @Positive
    private Integer limit;

    @FutureOrPresent(message = "Start date cannot be in the past")
    private ZonedDateTime start;

    @Future(message = "End date must be in the future")
    private ZonedDateTime end;

    private Boolean isAvailable;

    private Set<StudentDto> students;
}
