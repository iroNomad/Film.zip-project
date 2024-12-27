package com.crazy.filmzip.service;

import com.crazy.filmzip.dto.Movie;
import com.crazy.filmzip.dto.Video;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

@Service
public class DetailResponseService {

    private static final Logger logger = LoggerFactory.getLogger(DetailResponseService.class);
    private final OkHttpClient client;
    private final ObjectMapper mapper;

    public DetailResponseService(OkHttpClient client, ObjectMapper mapper) {
        this.client = client;
        this.mapper = mapper;
    }

    public Movie getMovieData(Request request) {

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            try (ResponseBody body = response.body()) {
                if (body == null) {
                    logger.error("Response body is null");
                    throw new IOException("Response body is null");
                }
                String jsonResponse = body.string();
                Movie movie = mapper.readValue(jsonResponse, Movie.class);
                logger.info("Successfully fetched movie data: {}", movie.getTitle());
                movie.setRating(String.format("%.1f", movie.getVoteAverage()));
                movie.setVoteCount(String.format("%,d", movie.getVoteCountRaw()));
                return movie;
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch movie data", e);
        }
    }

    public Video getVideoData(Request request) {

        Video videoObject = new Video();
        try (Response response = client.newCall(request).execute()) {
            String jsonResponse = response.body().string();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(jsonResponse);
            JsonNode results = rootNode.get("results");

            for (JsonNode video : results) {
                if ("Trailer".equals(video.get("type").asText()) && "YouTube".equals(video.get("site").asText())) {
                    String name = video.get("name").asText();
                    String key = video.get("key").asText();
                    videoObject.setName(name);
                    videoObject.setKey(key);
                    return videoObject;
                }
            }
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return videoObject;
    }
}
