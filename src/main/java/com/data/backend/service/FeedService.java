package com.data.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.data.backend.model.Feed;
import com.data.backend.model.dto.Creator;
import com.data.backend.model.ArgumentFeed;
import com.data.backend.repository.ArgumentRepository;
import com.data.backend.repository.ArgumentVoteRepository;
import com.data.backend.repository.CreatorRepository;
import com.data.backend.repository.TopicRepository;
import com.data.backend.repository.VoteRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedService {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private ArgumentRepository argumentRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private CreatorRepository creatorRepository;

    @Autowired
    private ArgumentVoteRepository argumentVoteRepository;

    public List<Feed> getFeed() {
        return topicRepository.findAll().stream().map(topic -> {
            Creator creator = creatorRepository.findById(topic.getCreatorId()).orElse(null);
            long upVotes = voteRepository.countByTopicIdAndVoteType(topic.getId(), "up");
            long downVotes = voteRepository.countByTopicIdAndVoteType(topic.getId(), "down");

            List<ArgumentFeed> arguments = argumentRepository.findByTopicId(topic.getId()).stream().map(argument -> {
                Creator argumentCreator = creatorRepository.findById(argument.getCreatorId()).orElse(null);
                long argumentUpVotes = argumentVoteRepository.countByArgumentIdAndVoteType(argument.getId(), "up");
                long argumentDownVotes = argumentVoteRepository.countByArgumentIdAndVoteType(argument.getId(), "down");

                return new ArgumentFeed(
                        argument.getId(),
                        argumentCreator,
                        argument.getTitle(),
                        argument.getDescription(),
                        argument.getDateCreated(),
                        argumentUpVotes,
                        argumentDownVotes,
                        argument.isSupporting());
            }).collect(Collectors.toList());

            return new Feed(
                    topic.getId(),
                    creator,
                    topic.getTitle(),
                    topic.getDescription(),
                    topic.getDateCreated(),
                    upVotes,
                    downVotes,
                    arguments);
        }).collect(Collectors.toList());
    }
}
