package com.custis.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "courses")
@Data
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "students_limit")
    private Integer limit;

    @Column(name = "start_time")
    private ZonedDateTime start;

    @Column(name = "end_time")
    private ZonedDateTime end;

    @Column(name = "is_available")
    private Boolean isAvailable;

    @Transient
    private Set<Student> students = new HashSet<>();
}