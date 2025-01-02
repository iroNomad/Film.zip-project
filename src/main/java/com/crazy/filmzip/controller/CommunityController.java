package com.crazy.filmzip.controller;

import com.crazy.filmzip.dto.CommunityListViewResponse;
import com.crazy.filmzip.dto.CommunityPostDetailResponse;
import com.crazy.filmzip.entity.Comment;
import com.crazy.filmzip.entity.CommunityPost;
import com.crazy.filmzip.service.CommentService;
import com.crazy.filmzip.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/community")
public class CommunityController {

    private final CommunityService communityService;
    private final CommentService commentService;

    /**
     * 게시글 목록 조회
     */
    @GetMapping("/list")
    public String getArticles(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "keyword", required = false) String keyword, Model model) {

        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Order.desc("createdAt")));

        Page<CommunityListViewResponse> articles;

        if (keyword != null && !keyword.isEmpty()) {
            articles = communityService.searchPosts(keyword, pageable); // 검색 로직 호출
        } else {
            articles = communityService.findAll(pageable); // 전체 게시글 조회
        }

        model.addAttribute("articles", articles.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", articles.getTotalPages());
        model.addAttribute("keyword", keyword); // 검색어 유지

        return "community/list";
    }

    /**
     * 게시글 상세 조회
     */
    @GetMapping("/{id}")
    public String getArticle(@PathVariable Long id, Model model) {
        communityService.incrementViewCount(id); // 조회수 증가
        CommunityPostDetailResponse article = communityService.findById(id); // findById 사용

        // 댓글 목록 가져오기
        List<Comment> comments = commentService.findByPostId(id);

        model.addAttribute("comments", comments);
        model.addAttribute("article", article);
        return "community/article";
    }

    /**
     * 게시글 등록/수정 페이지
     */
    @GetMapping("/new")
    public String newArticle(@RequestParam(required = false) Long id, Model model) {
        if (id == null) {
            // 신규 게시글
            model.addAttribute("article", new CommunityPostDetailResponse());
        } else {
            // 기존 게시글 수정
            CommunityPostDetailResponse article = communityService.findById(id); // findById 사용
            model.addAttribute("article", article);
        }
        return "community/new";
    }

    /**
     * 추천/비추천 처리
     */
    @PostMapping("/{id}/react")
    public String reactToPost(@PathVariable Long id, @RequestParam Long userId, @RequestParam String reactionType) {
        communityService.reactToPost(id, userId, reactionType);
        return "redirect:/community/" + id; // 상세 페이지로 리다이렉트
    }
}
