package com.data.backend.service;

import com.data.backend.model.Topic;
import com.data.backend.model.User;
import com.data.backend.model.dto.UpdateTopicRequest;
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

    /**
     * Creates a new topic and saves it to the database.
     *
     * @param topic Topic object to be created and saved.
     * @return Created and saved Topic object.
     */
    public Topic createTopic(Topic topic) {
        // Check if the creator_id exists in the users collection
        Optional<User> userRecord = userRepository.findById(topic.getCreator_id());
        // Print User
        System.out.println("User: " + userRecord);

        if (userRecord.isEmpty()) {
            throw new IllegalArgumentException("Invalid creator_id: User not found.");
        }

        // Retrieve the user's permissions
        User user = userRecord.get();

        // Print user and permissions
        System.out.println("User: " + user);

        // Verify the "contributor" field is set to true
        Boolean isContributor = user.getContributor();
        if (isContributor == null || !isContributor) {
            throw new IllegalArgumentException("User is not authorized to create topics.");
        }

        // Save the topic to the database
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
                        return idMap.get("_id").get(""); // Extract the plain ID
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

    public void updateTopic(UpdateTopicRequest request) {
        // Retrieve the topic from the database
        Topic topic = topicRepository.findById(request.getTopic_id())
                .orElseThrow(() -> new IllegalArgumentException("Topic not found."));

        // Retrieve the user from the database
        User user = userRepository.findById(request.getUser_id())
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        // Check if the user is authorized to update the topic
        if (isUserAuthorized(topic, user)) {
            // Update the topic's title and description
            if (request.getTitle() != null && !request.getTitle().isEmpty()) {
                topic.setTitle(request.getTitle());
            }

            if (request.getDescription() != null && !request.getDescription().isEmpty()) {
                topic.setDescription(request.getDescription());
            }

            // Save the updated topic to the database
            topicRepository.save(topic);
        } else {
            throw new IllegalArgumentException("You are not authorized to update this topic.");
        }
    }

    public void deleteTopic(String topic_id, String user_id) {
        // Retrieve the topic from the database
        Topic topic = topicRepository.findById(topic_id)
                .orElseThrow(() -> new IllegalArgumentException("Topic not found."));

        // Retrieve the user from the database
        User user = userRepository.findById(user_id)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        // Check if the user is authorized to delete the topic
        if (isUserAuthorized(topic, user)) {
            topicRepository.deleteById(topic_id);
        } else {
            throw new IllegalArgumentException("You are not authorized to delete this topic.");
        }
    }

    /**
     * Check if the user is authorized to delete the topic.
     */
    private boolean isUserAuthorized(Topic topic, User user) {
        // Check if the user is the creator
        if (topic.getCreator_id().equals(user.getId())) {
            return true;
        }

        // Check if the user has the required role
        return Boolean.TRUE.equals(user.getAdmin()
                || Boolean.TRUE.equals(user.getContributor())
                || Boolean.TRUE.equals(user.getModerator()));
    }

}
