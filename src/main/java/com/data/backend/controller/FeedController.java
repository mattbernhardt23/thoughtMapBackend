package com.data.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.data.backend.model.Feed;
import com.data.backend.service.FeedService;

@RestController
@RequestMapping("/api/feed")
public class FeedController {
    @Autowired
    private FeedService feedService;

    @GetMapping
    public List<Feed> getFeed() {
        return feedService.getFeed();
    }

}
