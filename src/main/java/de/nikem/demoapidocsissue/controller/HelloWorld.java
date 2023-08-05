package de.nikem.demoapidocsissue.controller;

import de.nikem.demoapidocsissue.HelloWorldDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;

@RestController
public class HelloWorld {

    @GetMapping("/")
    public HelloWorldDto hello() {
        var hello = new HelloWorldDto();
        hello.setGreeting("Hello World " + new Timestamp(System.currentTimeMillis()));
        return hello;
    }

}
