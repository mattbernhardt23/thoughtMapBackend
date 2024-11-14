package com.data.backend.service;

import com.data.backend.model.ArgumentVote;
import com.data.backend.repository.ArgumentVoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArgumentVoteService {

    @Autowired
    private ArgumentVoteRepository argumentVoteRepository;

    public List<ArgumentVote> getAllArgumentVotes() {
        return argumentVoteRepository.findAll();
    }

    public Optional<ArgumentVote> getArgumentVoteById(String id) {
        return argumentVoteRepository.findById(id);
    }

    public ArgumentVote createArgumentVote(ArgumentVote argumentVote) {
        return argumentVoteRepository.save(argumentVote);
    }

    public ArgumentVote updateArgumentVote(String id, ArgumentVote argumentVote) {
        argumentVote.setId(id);
        return argumentVoteRepository.save(argumentVote);
    }

    public void deleteArgumentVote(String id) {
        argumentVoteRepository.deleteById(id);
    }
}
