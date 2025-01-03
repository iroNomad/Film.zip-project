package com.crazy.filmzip.controller;

import com.crazy.filmzip.TmdbApiEndpoint;
import com.crazy.filmzip.dto.Movie;
import com.crazy.filmzip.dto.Video;
import com.crazy.filmzip.service.DetailResponseService;
import com.crazy.filmzip.service.GeneralResponseService;
import com.crazy.filmzip.service.WatchListService;
import okhttp3.HttpUrl;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
public class MainController {

    private final DetailResponseService detailResponseService;
    private final UserRepository userRepository;
    private final WatchListService watchListService;

    @Value("${themoviedb.api.key}")
    private String apiKey;

    // Constructor-based dependency injection
    public MainController(DetailResponseService detailResponseService, UserRepository userRepository, WatchListService watchListService) {
        this.detailResponseService = detailResponseService;
        this.userRepository = userRepository;
        this.watchListService = watchListService;
    }

    @GetMapping("/main")
    public String main(Model model) {

        HttpUrl trendingURL = HttpUrl.parse(TmdbApiEndpoint.TRENDING.getFullUrl() + "?language=ko");
        HttpUrl recommendedURL = HttpUrl.parse(TmdbApiEndpoint.DISCOVER.getFullUrl())
                .newBuilder()
                .addQueryParameter("language", "ko")
                .addQueryParameter("sort_by", "popularity.desc")
                .addQueryParameter("with_genres", "18 OR 28 OR 36")
                .build();

        List recommendedList = GeneralResponseService.responseHandler(createRequest(recommendedURL));
        List trendingList = GeneralResponseService.responseHandler(createRequest(trendingURL));

        List watchList = watchListService.getMovieListByUserID(1L);
        System.out.println("watchList = " + watchList);

        model.addAttribute("trendingList", trendingList);
        model.addAttribute("recommendedList", recommendedList);
        model.addAttribute("watchList", watchList);

        return "main"; // This points to templates/main.html
    }

    @GetMapping("/search/{pageNum}")
    public String search(@RequestParam("keyword") String keyword, @PathVariable("pageNum") int pageNum, Model model) {

        // Build the URL with the query parameter
        HttpUrl searchURL = HttpUrl.parse(TmdbApiEndpoint.SEARCH.getFullUrl())
                    .newBuilder()
                    .addQueryParameter("query", keyword)
                    .addQueryParameter("language", "ko")
                    .addQueryParameter("page", String.valueOf(pageNum))
                    .build();

        List movieList = GeneralResponseService.responseHandler(createRequest(searchURL));
        int totalPages = GeneralResponseService.getTotalPages();
        int startPage = Math.max(pageNum - 5, 1);
        int endPage = Math.min(pageNum + 4, totalPages);

        model.addAllAttributes(Map.of(
                "movies", movieList,
                "keyword", keyword,
                "totalPages", totalPages,
                "currentPage", pageNum,
                "startPage", startPage,
                "endPage", endPage
        ));
        return "search"; // This points to templates/search.html
    }

    @GetMapping("/detail/{movieID}")
    public String detail(@PathVariable("movieID") int movieID, Model model) {

        HttpUrl detailURL = HttpUrl.parse(TmdbApiEndpoint.DETAIL.getFullUrl() + movieID + "?language=ko");
        HttpUrl videoURL = HttpUrl.parse(TmdbApiEndpoint.DETAIL.getFullUrl() + movieID + "/videos?language=ko");

        Movie movie = detailResponseService.getMovieData(createRequest(detailURL));
        Video video = detailResponseService.getVideoData(createRequest(videoURL));
        boolean isInWatchList = detailResponseService.checkWatchList(movieID);

        // Pass the movie list to the view (detail.html)
        model.addAttribute("movie", movie);
        model.addAttribute("video", video);
        model.addAttribute("isInWatchList", isInWatchList);
        return "detail"; // This points to templates/detail.html
    }

    //createRequest function declaration
    public Request createRequest(HttpUrl url) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer " + apiKey)
                .build();
        return request;
    }
}
