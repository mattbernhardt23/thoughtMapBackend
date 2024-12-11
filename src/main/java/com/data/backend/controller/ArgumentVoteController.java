package com.data.backend.controller;

import com.data.backend.model.ArgumentVote;
import com.data.backend.service.ArgumentVoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/argument-votes")
public class ArgumentVoteController {

    @Autowired
    private ArgumentVoteService argumentVoteService;

    @GetMapping
    public List<ArgumentVote> getAllArgumentVotes() {
        return argumentVoteService.getAllArgumentVotes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArgumentVote> getArgumentVoteById(@PathVariable String id) {
        Optional<ArgumentVote> argumentVote = argumentVoteService.getArgumentVoteById(id);
        return argumentVote.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ArgumentVote createArgumentVote(@RequestBody ArgumentVote argumentVote) {
        return argumentVoteService.submitVote(argumentVote);
    }

    @PostMapping("/bulk")
    public List<ArgumentVote> createArgumentVotes() {
        return argumentVoteService.createArgumentVotes();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArgumentVote(@PathVariable String id) {
        argumentVoteService.deleteArgumentVote(id);
        return ResponseEntity.noContent().build();
    }
}
