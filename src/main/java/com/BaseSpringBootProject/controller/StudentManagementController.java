package com.BaseSpringBootProject.controller;

import com.BaseSpringBootProject.entities.Student;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("management/api/v1/students")
public class StudentManagementController implements Serializable {

    private static final List<Student> STUDENTS = Arrays.asList(
            new Student(1,"a"),
            new Student(2,"b"),
            new Student(3,"c")
    );

    @GetMapping
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @PreAuthorize("hasAnyAuthority('student:read')")
    public List<Student> getAllStudents(){
        return STUDENTS;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('student:write')")
    public void registerNewStudent(@RequestBody Student student){
        System.out.println(student);
    }

    @DeleteMapping(path = "{studentId}")
    @PreAuthorize("hasAnyAuthority('student:write')")
    public void deleteStudent(@PathVariable("studentId") Integer studentId){
        System.out.println(studentId);
    }

    @PutMapping(path = "{studentId}")
    @PreAuthorize("hasAnyAuthority('student:write')")
    public void updateStudent(@PathVariable("studentId") Integer studentId,@RequestBody Student student){
        System.out.println(String.format("%s %s",student.toString(),student.toString()));
    }

}
