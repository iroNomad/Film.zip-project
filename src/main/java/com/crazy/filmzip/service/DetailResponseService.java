package com.crazy.filmzip.service;

import com.crazy.filmzip.dto.Movie;
import com.crazy.filmzip.dto.Video;
import com.crazy.filmzip.entity.ToWatchMovie;
import com.crazy.filmzip.repository.WatchListRepository;
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
import java.util.List;

@Service
public class DetailResponseService {

    private static final Logger logger = LoggerFactory.getLogger(DetailResponseService.class);
    private final OkHttpClient client;
    private final ObjectMapper mapper;
    private final WatchListRepository watchListRepository;

    public DetailResponseService(OkHttpClient client, ObjectMapper mapper, WatchListRepository watchListRepository) {
        this.client = client;
        this.mapper = mapper;
        this.watchListRepository = watchListRepository;
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

            for (int i = results.size() - 1; i >= 0; i--) { // Iterate in reverse order to get first(main) trailer of the movie
                JsonNode video = results.get(i);
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

    public boolean checkWatchList(Long userId, Integer movieID) {

        List<ToWatchMovie> movieList = watchListRepository.findByUserIdOrderByAddedAtDesc(userId);
        if (movieList.isEmpty()) {
            return false;
        }

        for (ToWatchMovie movie : movieList) {
            System.out.println("getMovieID = " + movie.getMovieApiId());
            System.out.println("arg movieID = " + movieID);
            if (movie.getMovieApiId().equals(movieID)) {
                return true;
            }
        }
        return false;
    }
}
