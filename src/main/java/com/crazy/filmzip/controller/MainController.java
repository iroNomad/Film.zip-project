package com.crazy.filmzip.controller;

import com.crazy.filmzip.TmdbApiEndpoint;
import com.crazy.filmzip.dto.Movie;
import com.crazy.filmzip.service.DetailResponseService;
import com.crazy.filmzip.service.GeneralResponseService;
import okhttp3.HttpUrl;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
public class MainController {

    private final DetailResponseService detailResponseService;

    @Value("${themoviedb.api.key}")
    private String apiKey;

    // Constructor-based dependency injection
    public MainController(DetailResponseService detailResponseService) {
        this.detailResponseService = detailResponseService;
    }

    @GetMapping("/main")
    public String index(Model model) {
        Request request = new Request.Builder()
                .url(TmdbApiEndpoint.TRENDING.getFullUrl() + "?language=ko")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer " + apiKey)
                .build();

        List movieList = GeneralResponseService.responseHandler(request);

        // Pass the movie list to the view (index.html)
        model.addAttribute("movies", movieList);
        return "index"; // This points to templates/index.html
    }

    @GetMapping("/search")
    public String search(@RequestParam("keyword") String keyword, Model model) {

        // Build the URL with the query parameter
        HttpUrl url = HttpUrl.parse(TmdbApiEndpoint.SEARCH.getFullUrl())
                    .newBuilder()
                    .addQueryParameter("query", keyword)
                    .addQueryParameter("language", "ko")
                    .build();

        // Create the request
        Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("accept", "application/json")
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .build();

        List movieList = GeneralResponseService.responseHandler(request);

        // Pass the movie list to the view (search.html)
        model.addAttribute("movies", movieList);
        return "search"; // This points to templates/search.html
    }

    @GetMapping("/detail/{movieID}")
    public String detail(@PathVariable("movieID") String movieID, Model model) {
        // Create the request
        Request request = new Request.Builder()
                .url(TmdbApiEndpoint.DETAIL.getFullUrl() + movieID + "?language=ko")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer " + apiKey)
                .build();

        Movie movie = detailResponseService.getMovieData(request);

        // Pass the movie list to the view (detail.html)
        model.addAttribute("movie", movie);
        return "detail"; // This points to templates/detail.html
    }
}
