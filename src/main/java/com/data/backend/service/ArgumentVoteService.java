package com.data.backend.service;

import com.data.backend.model.ArgumentVote;
import com.data.backend.repository.ArgumentRepository;
import com.data.backend.repository.ArgumentVoteRepository;
import com.data.backend.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class ArgumentVoteService {

    @Autowired
    private ArgumentVoteRepository argumentVoteRepository;

    @Autowired
    private ArgumentRepository argumentRepository;

    @Autowired
    private UserRepository userRepository;

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

    public List<ArgumentVote> createArgumentVotes() {
        List<String> userIds = userRepository.findAllIds().stream()
                .map(id -> {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        @SuppressWarnings("unchecked")
                        Map<String, Map<String, String>> idMap = objectMapper.readValue(id,
                                Map.class);
                        return idMap.get("_id").get("$oid"); // Extract the plain ID
                    } catch (Exception e) {
                        throw new RuntimeException("Error parsing user ID", e);
                    }
                })
                .collect(Collectors.toList());

        List<String> argumentIds = argumentRepository.findAllIds().stream()
                .map(id -> {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        @SuppressWarnings("unchecked")
                        Map<String, Map<String, String>> idMap = objectMapper.readValue(id,
                                Map.class);
                        return idMap.get("_id").get("$oid"); // Extract the plain ID
                    } catch (Exception e) {
                        throw new RuntimeException("Error parsing user ID", e);
                    }
                })
                .collect(Collectors.toList());

        // Prepare a list to store votes
        List<ArgumentVote> votes = new ArrayList<>();

        // Random generator for vote types
        Random random = new Random();

        // Generate a vote for each user and each topic
        for (String topicId : argumentIds) {
            for (String userId : userIds) {
                ArgumentVote vote = new ArgumentVote();
                vote.setUser_id(userId); // Assign user ID
                vote.setArgument_id(topicId); // Assign topic ID
                vote.setVoteType(random.nextBoolean() ? "up" : "down");
                votes.add(vote); // Add vote to the list
            }
        }

        return argumentVoteRepository.saveAll(votes);

    }
}
