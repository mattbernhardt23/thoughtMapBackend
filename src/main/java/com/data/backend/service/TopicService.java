package com.data.backend.service;

import com.data.backend.model.Argument;
import com.data.backend.model.Topic;
import com.data.backend.model.User;
import com.data.backend.model.dto.UpdateTopicRequest;
import com.data.backend.repository.ArgumentRepository;
import com.data.backend.repository.ArgumentVoteRepository;
import com.data.backend.repository.TopicRepository;
import com.data.backend.repository.UserRepository;
import com.data.backend.repository.VoteRepository;
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

    @Autowired
    private ArgumentRepository argumentRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private ArgumentVoteRepository argumentVoteRepository;

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
        // Retrieve the user from the database
        User user = userRepository.findById(topic.getCreatorId())
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        // Check if the user is authorized to update the topic
        if (isUserAuthorized(topic, user)) {

            // Save the updated topic to the database
            topicRepository.save(topic);
        } else {
            throw new IllegalArgumentException("You are not authorized to update this topic.");
        }

        // Save the topic to the database
        return topicRepository.save(topic);
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
        // Print Params
        System.out.println("Topic ID: " + topic_id);
        System.out.println("User ID: " + user_id);
        // Retrieve the topic from the database
        Topic topic = topicRepository.findById(topic_id)
                .orElseThrow(() -> new IllegalArgumentException("Topic not found."));

        // Print Topic
        System.out.println("Topic: " + topic);
        // Retrieve the user from the database
        User user = userRepository.findById(user_id)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        // Print User
        System.out.println("User: " + user);
        // Check if the user is authorized to delete the topic
        if (isUserAuthorized(topic, user)) {
            // TODO:Delete All Associate Arguments, Votes, ArguemntVotes and Comments
            // Generate a List of Argument IDs Associated with the Topic
            // Generate a List of Argument IDs Associated with the Topic
            List<String> argumentIds = argumentRepository.findByTopicId(topic_id)
                    .stream()
                    .map(Argument::getId)
                    .collect(Collectors.toList());

            // Print Argument IDs
            System.out.println("Argument IDs: " + argumentIds);

            // Print Argument IDs
            System.out.println("Argument IDs: " + argumentIds);

            // Delete All Votes Associated with argumentIds
            argumentVoteRepository.deleteAllByArgumentIdIn(argumentIds);
            argumentRepository.deleteAllByTopicId(topic_id);
            voteRepository.deleteAllByTopicId(topic_id);

            // Delete the topic from the database
            topicRepository.deleteById(topic_id);
        } else {
            throw new IllegalArgumentException("You are not authorized to delete this topic.");
        }
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
            topic.setCreatorId(userIds.get(randomIndex)); // Assign the ObjectId as a string
        }

        // Save all topics to the database
        return topicRepository.saveAll(topics);
    }

    /**
     * Check if the user is authorized to delete the topic.
     */
    private boolean isUserAuthorized(Topic topic, User user) {
        // Check if the user is the creator
        if (topic.getCreatorId().equals(user.getId())) {
            return true;
        }

        // Check if the user has the required role
        return Boolean.TRUE.equals(user.getAdmin()
                || Boolean.TRUE.equals(user.getContributor())
                || Boolean.TRUE.equals(user.getModerator()));
    }

}
