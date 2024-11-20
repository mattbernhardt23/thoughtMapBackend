package com.data.backend.service;

import com.data.backend.model.Argument;
import com.data.backend.model.Topic;
import com.data.backend.repository.ArgumentRepository;
import com.data.backend.repository.TopicRepository;
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
public class ArgumentService {

    @Autowired
    private ArgumentRepository argumentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopicRepository topicRepository;

    public List<Argument> getAllArguments() {
        return argumentRepository.findAll();
    }

    public Optional<Argument> getArgumentById(String id) {
        return argumentRepository.findById(id);
    }

    public Argument createArgument(Argument argument) {
        return argumentRepository.save(argument);
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
                argument.setTopic_id(topicId); // Assign the topic ID
                argument.setCreator_id(userIds.get(random.nextInt(userIds.size()))); // Assign a random creator ID
                arguments.add(argument);
            }
        }

        // Save all arguments to the database
        return argumentRepository.saveAll(arguments);
    }

    public Argument updateArgument(String id, Argument argument) {
        argument.setId(id);
        return argumentRepository.save(argument);
    }

    public void deleteArgument(String id) {
        argumentRepository.deleteById(id);
    }
}
