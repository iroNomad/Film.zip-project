package com.crazy.filmzip.controller;

import com.crazy.filmzip.dto.CommunityListViewResponse;
import com.crazy.filmzip.dto.CommunityPostDetailResponse;
import com.crazy.filmzip.entity.CommunityPost;
import com.crazy.filmzip.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/community")
public class CommunityController {

    private final CommunityService communityService;

    @GetMapping("/list")
    public String getArticles(Model model) {
        List<CommunityListViewResponse> articles = communityService.findAll();
        model.addAttribute("articles", articles);

        return "community/list";
    }

    @GetMapping("/{id}")
    public String getArticle(@PathVariable Long id, Model model) {
        communityService.incrementViewCount(id); // 조회수 증가

        CommunityPostDetailResponse article = new CommunityPostDetailResponse(communityService.findById(id));
        model.addAttribute("article", article);

        return "community/article";
    }

    @GetMapping("/new")
    public String newArticle(@RequestParam(required = false) Long id, Model model) {
        if (id == null) {
            model.addAttribute("article", new CommunityPostDetailResponse());
        } else {
            CommunityPost post = communityService.findById(id);
            CommunityPostDetailResponse article = new CommunityPostDetailResponse(post);
            model.addAttribute("article", article);
        }

        return "community/new";
    }
}
