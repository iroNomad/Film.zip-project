package com.crazy.filmzip.controller;

import com.crazy.filmzip.TmdbApiEndpoint;
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

    private final OkHttpClient client = new OkHttpClient();

    @GetMapping("/main")
    public String index(Model model) {
        Request request = new Request.Builder()
                .url(TmdbApiEndpoint.TRENDING.getFullUrl(baseUrl))
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer " + apiKey)
                .build();

        List<Map<String, Object>> movieList = new ArrayList<>();

        try (Response response = client.newCall(request).execute()) {
            String jsonResponse = response.body().string();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(jsonResponse);
            JsonNode results = rootNode.get("results");

            for (JsonNode movie : results) {
                // Extract specific fields to send to the HTML page
                String title = movie.get("title").asText();
                String release_date = movie.get("release_date").asText();
                String poster_path = movie.get("poster_path").asText();
                if (release_date.isEmpty()) {
                    release_date = "NO DATA";
                }

                // Add each movie as a map (key-value pairs) to the list
                movieList.add(Map.of(
                        "title", title,
                        "release_date", release_date,
                        "poster_path", poster_path
                ));
            }

        } catch (IOException e) {
            return "Error occurred: " + e.getMessage();
        }

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
                    .build();

        // Create the request
        Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("accept", "application/json")
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .build();

        // Send the API request
        List<Map<String, Object>> movieList = new ArrayList<>();

        try (Response response = client.newCall(request).execute()) {
            String jsonResponse = response.body().string();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(jsonResponse);
            JsonNode results = rootNode.get("results");
            for (JsonNode movie : results) {
                // Extract specific fields to send to the HTML page
                String title = movie.get("title").asText();
                String release_date = movie.get("release_date").asText();
                String poster_path = movie.get("poster_path").asText();
                if (release_date.isEmpty()) {
                    release_date = "NO DATA";
                }

                if (movie.get("poster_path").isNull()) {
                    poster_path = "/images/Fallback_poster.png";
                }
                else {
                    poster_path = "https://image.tmdb.org/t/p/original" + poster_path;
                }
                // Add each movie as a map (key-value pairs) to the list
                movieList.add(Map.of(
                        "title", title,
                        "release_date", release_date,
                        "poster_path", poster_path
                ));
            }

        } catch (IOException e) {
            return "Error occurred: " + e.getMessage();
        }

        // Pass the movie list to the view (index.html)
        model.addAttribute("movies", movieList);
        return "search"; // This points to templates/index.html
    }
}
