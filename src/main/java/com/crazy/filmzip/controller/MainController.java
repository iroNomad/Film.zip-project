package com.crazy.filmzip.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class MainController {

    @Value("${themoviedb.api.key}")
    private String apiKey;

    private final OkHttpClient client = new OkHttpClient();

    @GetMapping("/main")
    public String index(Model model) {
        Request request = new Request.Builder()
                .url("https://api.themoviedb.org/3/trending/movie/day")
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

                // Add each movie as a map (key-value pairs) to the list
                movieList.add(Map.of(
                        "title", title,
                        "release_date", release_date
                ));
            }

        } catch (IOException e) {
            return "Error occurred: " + e.getMessage();
        }

        // Pass the movie list to the view (index.html)
        model.addAttribute("movies", movieList);
        return "index"; // This points to templates/index.html
    }
}
