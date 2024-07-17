package com.bonidev.foro_hub.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SecurityRequirement(name = "bearer-key")
public class HelloController {
    @GetMapping("/")
    public String HelloWorld() {
        return "Hello, World!";
    }
}
