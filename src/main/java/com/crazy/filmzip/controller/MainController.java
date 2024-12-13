package com.crazy.filmzip.controller;

import com.crazy.filmzip.TmdbApiEndpoint;
import com.crazy.filmzip.service.ResponseService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class MainController {

    @Value("${themoviedb.api.key}")
    private String apiKey;

    String baseUrl = "https://api.themoviedb.org/3/";

//    private final OkHttpClient client = new OkHttpClient();

    @GetMapping("/main")
    public String index(Model model) {
        Request request = new Request.Builder()
                .url(TmdbApiEndpoint.TRENDING.getFullUrl(baseUrl) + "?language=ko")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer " + apiKey)
                .build();

        List movieList = ResponseService.responseHandler(request);

        // Pass the movie list to the view (index.html)
        model.addAttribute("movies", movieList);
        return "index"; // This points to templates/index.html
    }

    @GetMapping("/search")
    public String search(@RequestParam("keyword") String keyword, Model model) {

        // Build the URL with the query parameter
        HttpUrl url = HttpUrl.parse(TmdbApiEndpoint.SEARCH.getFullUrl(baseUrl))
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

        List movieList = ResponseService.responseHandler(request);

        // Pass the movie list to the view (index.html)
        model.addAttribute("movies", movieList);
        return "search"; // This points to templates/index.html
    }
}
