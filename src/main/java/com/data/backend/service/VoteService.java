package com.data.backend.service;

import com.data.backend.model.Vote;
import com.data.backend.repository.TopicRepository;
import com.data.backend.repository.UserRepository;
import com.data.backend.repository.VoteRepository;
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
public class VoteService {

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Vote> getAllVotes() {
        return voteRepository.findAll();
    }

    public Optional<Vote> getVoteById(String id) {
        return voteRepository.findById(id);
    }

    public Vote submitVote(Vote incomingVote) {
        // Check if the user has already voted on this topic
        Optional<Vote> existingVote = voteRepository.findByUserIdAndTopicId(
                incomingVote.getUserId(), incomingVote.getTopicId());

        if (existingVote.isPresent()) {
            // If the same voteType is clicked, remove the vote (toggle behavior)
            if (existingVote.get().getVoteType().equalsIgnoreCase(incomingVote.getVoteType())) {
                voteRepository.deleteById(existingVote.get().getId());
                return null; // Vote removed
            } else {
                // Update the vote to the new type
                existingVote.get().setVoteType(incomingVote.getVoteType());
                return voteRepository.save(existingVote.get());
            }
        }

        // Create a new vote if none exists
        return voteRepository.save(incomingVote);
    }

    public void deleteAllByTopicId(String topicId) {
        voteRepository.deleteAllByTopicId(topicId);
    }

    public void deleteVote(String id) {
        voteRepository.deleteById(id);
    }

    public List<Vote> createVotes() {
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

        List<String> topicIds = topicRepository.findAllIds().stream()
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
        List<Vote> votes = new ArrayList<>();

        // Random generator for vote types
        Random random = new Random();

        // Generate a vote for each user and each topic
        for (String topicId : topicIds) {
            for (String userId : userIds) {
                Vote vote = new Vote();
                vote.setUserId(userId); // Assign user ID
                vote.setTopicId(topicId); // Assign topic ID
                vote.setVoteType(random.nextBoolean() ? "up" : "down");
                votes.add(vote); // Add vote to the list
            }
        }

        return voteRepository.saveAll(votes);
    }
}
