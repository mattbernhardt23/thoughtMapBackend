package com.data.backend.service;

import com.data.backend.model.Topic;
import com.data.backend.repository.TopicRepository;
import com.data.backend.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Topic> getAllTopics() {
        return topicRepository.findAll();
    }

    public Optional<Topic> getTopicById(String id) {
        return topicRepository.findById(id);
    }

    public Optional<Topic> getTopicByTitle(String title) {
        return topicRepository.findByTitle(title);
    }

    public Topic createTopic(Topic topic) {
        return topicRepository.save(topic);
    }

    /**
     * Assigns a random creator_id to each topic from the list of users and saves
     * them to the database.
     *
     * @param topics List of Topic objects to be updated and saved.
     * @return List of updated and saved Topic objects.
     */
    public List<Topic> createTopics(List<Topic> topics) {
        // Retrieve all user IDs as plain strings
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
        for (Topic topic : topics) {
            int randomIndex = random.nextInt(userIds.size());
            topic.setCreator_id(userIds.get(randomIndex)); // Assign the ObjectId as a string
        }

        // Save all topics to the database
        return topicRepository.saveAll(topics);
    }

    public Topic updateTopic(String id, Topic topic) {
        topic.setId(id);
        return topicRepository.save(topic);
    }

    public void deleteTopic(String id) {
        topicRepository.deleteById(id);
    }
}
