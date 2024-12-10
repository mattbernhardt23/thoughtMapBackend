package com.data.backend;

import com.data.backend.model.*;
import com.data.backend.model.dto.UpdateTopicRequest;
import com.data.backend.repository.*;
import com.data.backend.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class BackendApplicationTests {

	@Mock
	private UserRepository userRepository;
	@Mock
	private TopicRepository topicRepository;
	@Mock
	private ArgumentRepository argumentRepository;
	@Mock
	private VoteRepository voteRepository;
	@Mock
	private ArgumentVoteRepository argumentVoteRepository;

	@InjectMocks
	private UserService userService;
	@InjectMocks
	private TopicService topicService;
	@InjectMocks
	private ArgumentService argumentService;
	@InjectMocks
	private VoteService voteService;
	@InjectMocks
	private ArgumentVoteService argumentVoteService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void contextLoads() {
	}

	// TopicService Tests
	@Test
	void testGetAllTopics() {
		when(topicRepository.findAll()).thenReturn(Arrays.asList(new Topic()));
		assertFalse(topicService.getAllTopics().isEmpty());
	}

	@Test
	void testGetTopicById() {
		Topic topic = new Topic();
		topic.setId("1");
		when(topicRepository.findById("1")).thenReturn(Optional.of(topic));
		Optional<Topic> foundTopic = topicService.getTopicById("1");
		assertTrue(foundTopic.isPresent());
		assertEquals("1", foundTopic.get().getId());
	}

	@Test
	void testCreateTopic() {
		Topic topic = new Topic();
		when(topicRepository.save(topic)).thenReturn(topic);
		assertEquals(topic, topicService.createTopic(topic));
	}

	@Test
	void testUpdateTopic() {
		// Arrange
		String topicId = "1";
		String userId = "user123";

		// Create a mock topic and user
		Topic existingTopic = new Topic();
		existingTopic.setId(topicId);
		existingTopic.setCreator_id(userId);
		existingTopic.setTitle("Old Title");
		existingTopic.setDescription("Old Description");

		User mockUser = new User();
		mockUser.setId(userId);
		mockUser.setAdmin(true);
		mockUser.setContributor(true);
		mockUser.setModerator(true);

		// Create a request for updating the topic
		UpdateTopicRequest updateRequest = new UpdateTopicRequest();
		updateRequest.setTopic_id(topicId);
		updateRequest.setUser_id(userId);
		updateRequest.setTitle("Updated Title");
		updateRequest.setDescription("Updated Description");

		// Mock the repository calls
		when(topicRepository.findById(topicId)).thenReturn(Optional.of(existingTopic));
		when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
		when(topicRepository.save(any(Topic.class))).thenAnswer(invocation -> invocation.getArgument(0));

		// Act
		topicService.updateTopic(updateRequest);

		// Assert
		verify(topicRepository, times(1)).findById(topicId);
		verify(userRepository, times(1)).findById(userId);
		verify(topicRepository, times(1)).save(existingTopic);

		assertEquals("Updated Title", existingTopic.getTitle());
		assertEquals("Updated Description", existingTopic.getDescription());
	}

	@Test
	void testDeleteTopic() {
		topicService.deleteTopic("1", "1");
		verify(topicRepository, times(1)).deleteById("1");
	}

	// ArgumentService Tests
	@Test
	void testGetAllArguments() {
		when(argumentRepository.findAll()).thenReturn(Arrays.asList(new Argument()));
		assertFalse(argumentService.getAllArguments().isEmpty());
	}

	@Test
	void testGetArgumentById() {
		Argument argument = new Argument();
		argument.setId("1");
		when(argumentRepository.findById("1")).thenReturn(Optional.of(argument));
		Optional<Argument> foundArgument = argumentService.getArgumentById("1");
		assertTrue(foundArgument.isPresent());
		assertEquals("1", foundArgument.get().getId());
	}

	@Test
	void testCreateArgument() {
		Argument argument = new Argument();
		when(argumentRepository.save(argument)).thenReturn(argument);
		assertEquals(argument, argumentService.createArgument(argument));
	}

	@Test
	void testUpdateArgument() {
		Argument argument = new Argument();
		argument.setId("1");
		when(argumentRepository.save(argument)).thenReturn(argument);
		assertEquals(argument, argumentService.updateArgument("1", argument));
	}

	@Test
	void testDeleteArgument() {
		argumentService.deleteArgument("1");
		verify(argumentRepository, times(1)).deleteById("1");
	}

	// VoteService Tests
	@Test
	void testGetAllVotes() {
		when(voteRepository.findAll()).thenReturn(Arrays.asList(new Vote()));
		assertFalse(voteService.getAllVotes().isEmpty());
	}

	@Test
	void testGetVoteById() {
		Vote vote = new Vote();
		vote.setId("1");
		when(voteRepository.findById("1")).thenReturn(Optional.of(vote));
		Optional<Vote> foundVote = voteService.getVoteById("1");
		assertTrue(foundVote.isPresent());
		assertEquals("1", foundVote.get().getId());
	}

	@Test
	void testCreateVote() {
		Vote vote = new Vote();
		when(voteRepository.save(vote)).thenReturn(vote);
		assertEquals(vote, voteService.createVote(vote));
	}

	@Test
	void testUpdateVote() {
		Vote vote = new Vote();
		vote.setId("1");
		when(voteRepository.save(vote)).thenReturn(vote);
		assertEquals(vote, voteService.updateVote("1", vote));
	}

	@Test
	void testDeleteVote() {
		voteService.deleteVote("1");
		verify(voteRepository, times(1)).deleteById("1");
	}

	// ArgumentVoteService Tests
	@Test
	void testGetAllArgumentVotes() {
		when(argumentVoteRepository.findAll()).thenReturn(Arrays.asList(new ArgumentVote()));
		assertFalse(argumentVoteService.getAllArgumentVotes().isEmpty());
	}

	@Test
	void testGetArgumentVoteById() {
		ArgumentVote argumentVote = new ArgumentVote();
		argumentVote.setId("1");
		when(argumentVoteRepository.findById("1")).thenReturn(Optional.of(argumentVote));
		Optional<ArgumentVote> foundArgumentVote = argumentVoteService.getArgumentVoteById("1");
		assertTrue(foundArgumentVote.isPresent());
		assertEquals("1", foundArgumentVote.get().getId());
	}

	@Test
	void testCreateArgumentVote() {
		ArgumentVote argumentVote = new ArgumentVote();
		when(argumentVoteRepository.save(argumentVote)).thenReturn(argumentVote);
		assertEquals(argumentVote, argumentVoteService.createArgumentVote(argumentVote));
	}

	@Test
	void testUpdateArgumentVote() {
		ArgumentVote argumentVote = new ArgumentVote();
		argumentVote.setId("1");
		when(argumentVoteRepository.save(argumentVote)).thenReturn(argumentVote);
		assertEquals(argumentVote, argumentVoteService.updateArgumentVote("1", argumentVote));
	}

	@Test
	void testDeleteArgumentVote() {
		argumentVoteService.deleteArgumentVote("1");
		verify(argumentVoteRepository, times(1)).deleteById("1");
	}
}
