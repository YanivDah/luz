package com.yaniv.luz;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/api/hello")
    public String hello() {
        return "Hello From Luz!";
    }

    @GetMapping("api/status")
    public String status() {
        return "Luz is running";
    }
}