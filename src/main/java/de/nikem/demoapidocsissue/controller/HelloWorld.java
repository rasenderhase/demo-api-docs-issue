package de.nikem.demoapidocsissue.controller;

import de.nikem.demoapidocsissue.dto.HelloWorldDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;

@RestController
public class HelloWorld {

    @GetMapping("/api/hello")
    public HelloWorldDto hello() {
        var hello = new HelloWorldDto();
        hello.setGreeting("Hello World " + new Timestamp(System.currentTimeMillis()));
        return hello;
    }

}
