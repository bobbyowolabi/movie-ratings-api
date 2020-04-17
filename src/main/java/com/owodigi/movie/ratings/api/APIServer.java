package com.owodigi.movie.ratings.api;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@SpringBootApplication
@RestController
public class APIServer {

    @GetMapping("/movie-ratings")
    public String hello(@RequestParam(value = "title", required = true) final String title) {
        return "{\"result\":\"random value to see if the plumbin works!\"}";
    }
}