package com.crazy.filmzip.controller;

import com.crazy.filmzip.dto.CommunityListViewResponse;
import com.crazy.filmzip.dto.CommunityPostDetailResponse;
import com.crazy.filmzip.entity.CommunityPost;
import com.crazy.filmzip.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/community")
public class CommunityController {

    private final CommunityService communityService;

    /**
     * 게시글 목록 조회
     */
    @GetMapping("/list")
    public String getArticles(Model model) {
        List<CommunityListViewResponse> articles = communityService.findAll();
        model.addAttribute("articles", articles);
        return "community/list";
    }

    /**
     * 게시글 상세 조회
     */
    @GetMapping("/{id}")
    public String getArticle(@PathVariable Long id, Model model) {
        communityService.incrementViewCount(id); // 조회수 증가
        CommunityPostDetailResponse article = communityService.findById(id); // findById 사용
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
