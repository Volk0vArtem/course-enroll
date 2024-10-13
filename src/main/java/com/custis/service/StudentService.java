package com.custis.service;

import com.custis.dto.StudentDto;
import com.custis.dto.mapper.StudentMapper;
import com.custis.exception.NotFoundException;
import com.custis.model.Student;
import com.custis.repository.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentMapper studentMapper;
    private final StudentRepository studentRepository;

    @Transactional
    public StudentDto addStudent(StudentDto studentDto) {
        return studentMapper.toStudentDto(studentRepository.save(studentMapper.toStudent(studentDto)));
    }

    public StudentDto getStudentById(Long id) {
        return studentMapper.toStudentDto(studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Student with id=" + id + "not found")));
    }

    public List<StudentDto> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(studentMapper::toStudentDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    @Transactional
    public StudentDto patchStudent(StudentDto studentDto, Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Student with id=" + id + "not found"));
        Student patchedStudent = studentMapper.toStudent(studentDto);

        if (patchedStudent.getName() != null) {
            student.setName(patchedStudent.getName());
        }
        if (patchedStudent.getEmail() != null) {
            student.setEmail(patchedStudent.getEmail());
        }

        return studentMapper.toStudentDto(studentRepository.save(student));
    }
}