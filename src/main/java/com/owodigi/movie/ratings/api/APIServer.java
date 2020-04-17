package com.owodigi.movie.ratings.api;

import com.owodigi.movie.ratings.MovieRatingsApp;
import com.owodigi.movie.ratings.api.domain.RatingRecord;
import java.io.IOException;
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
    public RatingRecord movieRatings(@RequestParam(value = "title", required = true) final String title) throws IOException {
        return MovieRatingsApp.store().title(title);
    }
}