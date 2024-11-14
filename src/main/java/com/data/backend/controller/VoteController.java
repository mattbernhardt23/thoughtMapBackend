package com.data.backend.controller;

import com.data.backend.model.Vote;
import com.data.backend.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
        return voteService.createVote(vote);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vote> updateVote(@PathVariable String id, @RequestBody Vote vote) {
        Vote updatedVote = voteService.updateVote(id, vote);
        return ResponseEntity.ok(updatedVote);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVote(@PathVariable String id) {
        voteService.deleteVote(id);
        return ResponseEntity.noContent().build();
    }
}
