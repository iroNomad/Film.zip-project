package com.crazy.filmzip.controller;

import com.crazy.filmzip.dto.CommunityListViewResponse;
import com.crazy.filmzip.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/community")
public class CommunityController {

    private final CommunityService communityService;

    @GetMapping("/list")
    public String list(Model model){

        List<CommunityListViewResponse> posts = communityService.findAll().stream()
                .map(CommunityListViewResponse::new)
                .toList();

        model.addAttribute("posts", posts);

        return "community/list";
    }

}
