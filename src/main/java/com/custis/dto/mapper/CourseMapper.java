package com.custis.dto.mapper;

import com.custis.dto.CourseDto;
import com.custis.model.Course;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    Course toCourse(CourseDto courseDto);

    CourseDto toCourseDto(Course course);
}
