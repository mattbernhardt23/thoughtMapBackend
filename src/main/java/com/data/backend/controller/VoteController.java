package com.data.backend.controller;

import com.data.backend.model.Vote;
import com.data.backend.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/votes")
public class VoteController {

    @Autowired
    private VoteService voteService;

    @GetMapping
    public List<Vote> getAllVotes() {
        return voteService.getAllVotes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vote> getVoteById(@PathVariable String id) {
        Optional<Vote> vote = voteService.getVoteById(id);
        return vote.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Vote createVote(@RequestBody Vote vote) {
        return voteService.submitVote(vote);
    }

    @PostMapping("/delete/all")
    public ResponseEntity<String> deleteAllByTopicId(@RequestBody String topicId) {
        // TODO: process POST request
        voteService.deleteAllByTopicId(topicId);
        return ResponseEntity.ok("Votes deleted successfully.");
    }

    @PostMapping("/bulk")
    public List<Vote> createVotes() {
        return voteService.createVotes();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVote(@PathVariable String id) {
        voteService.deleteVote(id);
        return ResponseEntity.noContent().build();
    }
}
