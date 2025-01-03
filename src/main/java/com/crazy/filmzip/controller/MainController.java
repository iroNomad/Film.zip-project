package com.crazy.filmzip.controller;

import com.crazy.filmzip.TmdbApiEndpoint;
import com.crazy.filmzip.dto.Movie;
import com.crazy.filmzip.dto.Video;
import com.crazy.filmzip.entity.User;
import com.crazy.filmzip.service.DetailResponseService;
import com.crazy.filmzip.service.GeneralResponseService;
import com.crazy.filmzip.service.UserService;
import com.crazy.filmzip.service.WatchListService;
import okhttp3.HttpUrl;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MainController {

    private final DetailResponseService detailResponseService;
    private final WatchListService watchListService;
    private final UserService userService;


    @Value("${themoviedb.api.key}")
    private String apiKey;

    // Constructor-based dependency injection

    public MainController(DetailResponseService detailResponseService, WatchListService watchListService, UserService userService) {
        this.detailResponseService = detailResponseService;
        this.watchListService = watchListService;

        this.userService = userService;
    }

    @GetMapping("/api/main")
    public ResponseEntity<Map<String, Object>> main(Principal principal) {
        System.out.println("/api/main");

        System.out.println("principal: " + principal);

        User user = getCurrentUser();

        System.out.println("user: " + user);

        String genres = user.getGenre();

        // 예시) TMDB API 등으로부터 받아온 데이터
        HttpUrl trendingURL = HttpUrl.parse(TmdbApiEndpoint.TRENDING.getFullUrl() + "?language=ko");
        HttpUrl recommendedURL = HttpUrl.parse(TmdbApiEndpoint.DISCOVER.getFullUrl())
                .newBuilder()
                .addQueryParameter("language", "ko")
                .addQueryParameter("sort_by", "popularity.desc")
                .addQueryParameter("with_genres", genres)
                .build();

        List<?> recommendedList = GeneralResponseService.responseHandler(createRequest(recommendedURL));
        List<?> trendingList = GeneralResponseService.responseHandler(createRequest(trendingURL));

        // watchList는 로그인 사용자 기반으로 불러오도록 구현
        // 예시로 1L(하드코딩) -> 실제로는 user.getId() 등으로 처리
        List<?> watchList = watchListService.getMovieListByUserID(1L);

        // 디버깅 출력
        System.out.println(">>> trendingList = " + trendingList);
        System.out.println(">>> recommendedList = " + recommendedList);
        System.out.println(">>> watchList = " + watchList);

        // JSON으로 내려줄 Map 구성
        Map<String, Object> result = new HashMap<>();
        result.put("trendingList", trendingList);
        result.put("recommendedList", recommendedList);
        result.put("watchList", watchList);

        // 200 OK와 함께 Map 반환
        return ResponseEntity.ok(result);
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

    // 현재 인증된 사용자 가져오기
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authentication: " + authentication);

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("현재 사용자 인증 정보가 없습니다.");
        }

        String email = authentication.getName();
        System.out.println("Authenticated User Email: " + email);

        return userService.findByEmail(email);
    }
}
