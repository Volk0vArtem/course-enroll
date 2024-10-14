package com.custis.service;

import com.custis.dto.StudentDto;
import com.custis.exception.NotFoundException;
import com.custis.model.Student;
import com.custis.repository.StudentRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class StudentServiceIntegrationTest {

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentRepository studentRepository;

    @BeforeEach
    void setUp() {
        studentRepository.deleteAll();
    }

    @Test
    void testAddStudent() {
        StudentDto studentDto = new StudentDto();
        studentDto.setName("Student1");
        studentDto.setEmail("student1@gmail.com");

        StudentDto savedStudent = studentService.addStudent(studentDto);

        assertThat(savedStudent).isNotNull();
        assertThat(savedStudent.getId()).isNotNull();
        assertThat(savedStudent.getName()).isEqualTo("Student1");
        assertThat(savedStudent.getEmail()).isEqualTo("student1@gmail.com");
    }

    @Test
    void testGetStudentById() {
        Student student = new Student();
        student.setName("Student1");
        student.setEmail("student1@gmail.com");
        studentRepository.save(student);

        StudentDto retrievedStudent = studentService.getStudentById(student.getId());

        assertThat(retrievedStudent).isNotNull();
        assertThat(retrievedStudent.getId()).isEqualTo(student.getId());
        assertThat(retrievedStudent.getName()).isEqualTo(student.getName());
        assertThat(retrievedStudent.getEmail()).isEqualTo(student.getEmail());
    }

    @Test
    void testGetStudentByIdNotFound() {
        assertThrows(NotFoundException.class, () -> studentService.getStudentById(999L));
    }

    @Test
    void testGetAllStudents() {
        Student student1 = new Student();
        student1.setName("Student1");
        student1.setEmail("student1@gmail.com");
        studentRepository.save(student1);

        Student student2 = new Student();
        student2.setName("Student2");
        student2.setEmail("student2@gmail.com");
        studentRepository.save(student2);

        List<StudentDto> students = studentService.getAllStudents();

        assertEquals(2, students.size());
        assertThat(students.get(0).getName()).isEqualTo("Student1");
        assertThat(students.get(1).getName()).isEqualTo("Student2");
    }

    @Test
    void testDeleteStudent() {
        Student student = new Student();
        student.setName("Student1");
        student.setEmail("student1@gmail.com");
        studentRepository.save(student);

        studentService.deleteStudent(student.getId());

        assertThat(studentRepository.findById(student.getId())).isEmpty();
    }

    @Test
    void testPatchStudent() {
        Student student = new Student();
        student.setName("Student1");
        student.setEmail("student1@gmail.com");
        studentRepository.save(student);

        StudentDto patchDto = new StudentDto();
        patchDto.setName("Student2");
        patchDto.setEmail("student2@gmail.com");

        StudentDto patchedStudent = studentService.patchStudent(patchDto, student.getId());

        assertThat(patchedStudent.getName()).isEqualTo("Student2");
        assertThat(patchedStudent.getEmail()).isEqualTo("student2@gmail.com");
    }

    @Test
    void testPatchStudentNotFound() {
        StudentDto patchDto = new StudentDto();
        patchDto.setName("Student1");

        assertThrows(NotFoundException.class, () -> studentService.patchStudent(patchDto, 999L));
    }
}