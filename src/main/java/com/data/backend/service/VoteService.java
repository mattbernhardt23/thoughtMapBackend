package com.data.backend.service;

import com.data.backend.model.Vote;
import com.data.backend.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VoteService {

    @Autowired
    private VoteRepository voteRepository;

    public List<Vote> getAllVotes() {
        return voteRepository.findAll();
    }

    public Optional<Vote> getVoteById(String id) {
        return voteRepository.findById(id);
    }

    public Vote createVote(Vote vote) {
        return voteRepository.save(vote);
    }

    public Vote updateVote(String id, Vote vote) {
        vote.setId(id);
        return voteRepository.save(vote);
    }

    public void deleteVote(String id) {
        voteRepository.deleteById(id);
    }
}
