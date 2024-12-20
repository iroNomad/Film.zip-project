package com.crazy.filmzip.service;

import com.crazy.filmzip.dto.Movie;
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
                return movie;
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch movie data", e);
        }
    }
}
