package com.data.backend.service;

import com.data.backend.model.Argument;
import com.data.backend.model.Topic;
import com.data.backend.model.User;
import com.data.backend.model.dto.UpdateArgumentRequest;
import com.data.backend.repository.ArgumentRepository;
import com.data.backend.repository.ArgumentVoteRepository;
import com.data.backend.repository.TopicRepository;
import com.data.backend.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class ArgumentService {

    @Autowired
    private ArgumentRepository argumentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private ArgumentVoteRepository argumentVoteRepository;

    public List<Argument> getAllArguments() {
        return argumentRepository.findAll();
    }

    public Optional<Argument> getArgumentById(String id) {
        return argumentRepository.findById(id);
    }

    public Argument createArgument(Argument argument) {
        // Verify the topic exists
        Optional<Topic> topic = topicRepository.findById(argument.getTopicId());
        if (topic.isEmpty()) {
            throw new IllegalArgumentException("The associated topic does not exist.");
        }

        // Retrieve the user
        User user = userRepository.findById(argument.getCreatorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));

        // Check if the user is authorized
        if (!isUserAuthorized(argument, user)) {
            throw new IllegalArgumentException("You are not authorized to create an argument.");
        }

        // Save the argument to the database
        return argumentRepository.save(argument);
    }

    public void updateArgument(UpdateArgumentRequest request) {
        // Retrieve the topic from the database
        Argument argument = argumentRepository.findById(request.getArgumentId())
                .orElseThrow(() -> new IllegalArgumentException("Argument not found."));

        // Retrieve the user from the database
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));

        // Check if the user is authorized to update the topic
        if (isUserAuthorized(argument, user)) {
            // Update the topic's title and description
            if (request.getTitle() != null && !request.getTitle().isEmpty()) {
                argument.setTitle(request.getTitle());
            }

            if (request.getDescription() != null && !request.getDescription().isEmpty()) {
                argument.setDescription(request.getDescription());
            }

            // Save the updated topic to the database
            argumentRepository.save(argument);
        } else {
            throw new IllegalArgumentException("You are not authorized to update this argument.");
        }
    }

    public void deleteArgument(String argumentId, String userId) {
        // Verify the argument exists
        Argument argument = argumentRepository.findById(argumentId)
                .orElseThrow(() -> new IllegalArgumentException("Argument not found."));
        ;

        // Retrieve the user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));

        // Check if the user is authorized
        if (!isUserAuthorized(argument, user)) {
            throw new IllegalArgumentException("You are not authorized to create an argument.");
        }
        // TODO:Delete all Votes and Comments Associated with the Argument
        argumentVoteRepository.deleteAllByArgumentId(argumentId);

        argumentRepository.deleteById(argumentId);
    }

    /**
     * Assigns a random creator_id to each argument from the list of users and saves
     * them to the database.
     *
     * @param argument List of Argument objects to be updated and saved.
     * @return List of updated and saved Argument objects.
     */
    public List<Argument> createArguments(List<Map<String, Object>> input) {
        List<String> userIds = userRepository.findAllIds().stream()
                .map(id -> {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        @SuppressWarnings("unchecked")
                        Map<String, Map<String, String>> idMap = objectMapper.readValue(id, Map.class);
                        return idMap.get("_id").get("$oid"); // Extract the plain ID
                    } catch (Exception e) {
                        throw new RuntimeException("Error parsing user ID", e);
                    }
                })
                .collect(Collectors.toList());

        // Randomly assign a creator_id to each topic
        Random random = new Random();

        // Prepare a list to collect arguments
        List<Argument> arguments = new ArrayList<>();

        // Loop over input
        for (Map<String, Object> entry : input) {
            // Extract the key (topic title)
            String topicTitle = (String) entry.get("key");

            // Look up the Topic ID by title
            String topicId = topicRepository.findByTitle(topicTitle)
                    .map(Topic::getId)
                    .orElseThrow(() -> new RuntimeException("Topic not found for title: " + topicTitle));

            // Get the list of argument maps
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> argumentList = (List<Map<String, Object>>) entry.get("arguments");

            // Transform each argument map into an Argument object
            for (Map<String, Object> argData : argumentList) {
                Argument argument = new Argument();
                argument.setTitle((String) argData.get("title"));
                argument.setDescription((String) argData.get("description"));
                argument.setSupporting((Boolean) argData.get("supporting"));
                argument.setTopicId(topicId); // Assign the topic ID
                argument.setCreatorId(userIds.get(random.nextInt(userIds.size()))); // Assign a random creator ID
                arguments.add(argument);
            }
        }

        // Save all arguments to the database
        return argumentRepository.saveAll(arguments);
    }

    /**
     * Check if the user is authorized to delete the topic.
     */
    private boolean isUserAuthorized(Argument argument, User user) {
        // Check if the user is the creator
        if (argument.getCreatorId().equals(user.getId())) {
            return true;
        }

        // Check if the user has the required role
        return Boolean.TRUE.equals(user.getAdmin()
                || Boolean.TRUE.equals(user.getContributor())
                || Boolean.TRUE.equals(user.getModerator()));
    }

}
