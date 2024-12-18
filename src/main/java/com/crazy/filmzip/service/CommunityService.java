package com.crazy.filmzip.service;

import com.crazy.filmzip.entity.CommunityPost;
import com.crazy.filmzip.repository.CommunityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommunityService {
    private final CommunityRepository communityRepository;

    public List<CommunityPost> findAll(){
        return communityRepository.findAll();
    }
}
