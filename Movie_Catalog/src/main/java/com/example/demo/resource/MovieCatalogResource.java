package com.example.demo.resource;

import com.example.demo.model.CatalogItem;
import com.example.demo.model.Movie;
import com.example.demo.model.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable String userId) {
        //return Collections.singletonList(new CatalogItem("Dark Night", "description", 5));
        //RestTemplate restTemplate = new RestTemplate();

        // get all rated movieID
        UserRating userRating = restTemplate.getForObject("http://localhost:8083/ratingdata/users/" + userId, UserRating.class);

        // for each movieId, call movie info service and get detail (movie name)

//        return userRating.getUserRating().stream().map(rating -> {
//
//            Movie movie = restTemplate.getForObject("http://localhost:8082/movies/" + rating.getMovieId(), Movie.class);
//
//            return new CatalogItem(movie.getName(),"this is description", rating.getRating());
//
//        }).collect(Collectors.toList());

        List<CatalogItem> catalogItems = new ArrayList<>();

        userRating.getUserRating().forEach(rating -> {

            Movie movie = restTemplate.getForObject("http://localhost:8082/movies/" + rating.getMovieId(), Movie.class);

            CatalogItem catalogItem = new CatalogItem(movie.getName(), "this is description", rating.getRating());

            catalogItems.add(catalogItem);

        });

        return catalogItems;
    }

}
