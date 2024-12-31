package com.crazy.filmzip.service;

import com.crazy.filmzip.dto.CommentRequest;
import com.crazy.filmzip.dto.CommentResponse;
import com.crazy.filmzip.entity.User;
import com.crazy.filmzip.repository.CommentRepository;
import com.crazy.filmzip.repository.CommunityRepository;
import com.crazy.filmzip.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommunityRepository communityRepository;

}
