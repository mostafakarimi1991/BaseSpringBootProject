package com.BaseSpringBootProject.controller;

import com.BaseSpringBootProject.dto.LoginDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class LoginController {

    @PostMapping("login")
    public String getLogin(@RequestBody LoginDto loginDto){
        return loginDto.toString();
    }

    @GetMapping("courses")
    public ResponseEntity<String> getCourses(){
        return new ResponseEntity<>("Courses", HttpStatus.OK);
    }

    @GetMapping("mostafa")
    public ResponseEntity<String> getMostafa(){
        return new ResponseEntity<>("Mostafa", HttpStatus.OK);
    }
}
