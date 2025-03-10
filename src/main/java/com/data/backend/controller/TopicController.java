package com.data.backend.controller;

import com.data.backend.model.Topic;
import com.data.backend.model.dto.DeleteTopicRequest;
import com.data.backend.model.dto.UpdateTopicRequest;
import com.data.backend.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/topic")
public class TopicController {

    @Autowired
    private TopicService topicService;

    @GetMapping
    public List<Topic> getAllTopics() {
        return topicService.getAllTopics();
    }

    @GetMapping("/get-topic-by-id")
    public ResponseEntity<Topic> getTopicById(@RequestParam("topic_id") String topicId) {
        Optional<Topic> topic = topicService.getTopicById(topicId);
        return topic.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Topic createTopic(@RequestBody Topic topic) {
        return topicService.createTopic(topic);
    }

    @PostMapping("/bulk")
    public List<Topic> createTopics(@RequestBody List<Topic> topics) {
        return topicService.createTopics(topics);
    }

    @DeleteMapping("/id")
    public ResponseEntity<String> deleteTopic(@RequestBody DeleteTopicRequest request) {
        try {
            // Print Inside Try Block
            System.out.println("Inside Try Block");
            // Use the fields from the request object
            topicService.deleteTopic(request.getTopicId(), request.getUserId());
            return ResponseEntity.ok("Topic deleted successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
        }
    }

    @PutMapping("/id")
    public ResponseEntity<String> updateTopic(@RequestBody UpdateTopicRequest request) {
        try {
            // Use the fields from the request object
            topicService.updateTopic(request);
            return ResponseEntity.ok("Topic updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
        }
    }

}
