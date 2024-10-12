package com.custis.dto.mapper;

import com.custis.dto.StudentDto;
import com.custis.model.Student;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    Student toStudent(StudentDto studentDto);

    StudentDto toStudentDto(Student student);
}
