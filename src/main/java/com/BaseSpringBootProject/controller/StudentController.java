package com.BaseSpringBootProject.controller;

import com.BaseSpringBootProject.entities.Student;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/students")

public class StudentController {

    private static final List<Student> STUDENTS = Arrays.asList(
      new Student(1,"a"),
      new Student(2,"b"),
      new Student(3,"c")
    );

    @GetMapping(path = "/{studentId}")
    public Student getStudent(@PathVariable Integer studentId){
        return STUDENTS.stream()
                .filter(student -> studentId.equals(student.getId()))
                .findFirst()
                .orElseThrow(()-> new IllegalStateException("Student" + studentId+ " does not exist"));
    }
}
