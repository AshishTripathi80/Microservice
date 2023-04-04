package com.moviecatlogservice.resources;


import com.moviecatlogservice.models.CatalogItem;
import com.moviecatlogservice.models.Movie;
import com.moviecatlogservice.models.Rating;
import com.moviecatlogservice.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {
    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){

        UserRating ratings= restTemplate.getForObject("http://rating-data-service/ratingsdata/users/"+userId, UserRating.class);
        return ratings.getUserRating().stream().map(rating -> {
            // for each movie id, call movie info service and get details
            Movie movie=restTemplate.getForObject("http://movie-info-service/movies/foo"+rating.getMovieId(), Movie.class);
            //put them all together
            return new CatalogItem(movie.getName(),"Test",rating.getRating());
        })
        .collect(Collectors.toList());
    }
}
