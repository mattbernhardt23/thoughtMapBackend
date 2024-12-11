package com.data.backend;

import com.data.backend.model.*;
import com.data.backend.model.dto.UpdateArgumentRequest;
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
	void testCreateTopic_Success() {
		// Arrange
		Topic topic = new Topic();
		topic.setId("topic1");
		topic.setCreatorId("user123");
		topic.setTitle("New Topic");
		topic.setDescription("This is a new topic.");

		User user = new User();
		user.setId("user123");
		user.setAdmin(true);
		user.setContributor(true);
		user.setModerator(true);

		when(userRepository.findById("user123")).thenReturn(Optional.of(user));
		when(topicRepository.save(topic)).thenReturn(topic);

		// Act
		Topic createdTopic = topicService.createTopic(topic);

		// Assert
		assertEquals(topic, createdTopic);
		verify(userRepository, times(1)).findById("user123");
		verify(topicRepository, times(1)).save(topic);
	}

	@Test
	void testUpdateTopic() {
		// Arrange
		String topicId = "1";
		String userId = "user123";

		// Create a mock topic and user
		Topic existingTopic = new Topic();
		existingTopic.setId(topicId);
		existingTopic.setCreatorId(userId);
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
	void testDeleteTopic_Success() {
		// Arrange
		String topicId = "topic123";
		String userId = "user123";

		Topic topic = new Topic();
		topic.setId(topicId);
		topic.setCreatorId(userId);

		User user = new User();
		user.setId(userId);
		user.setAdmin(true);
		user.setContributor(true);
		user.setModerator(true);

		when(topicRepository.findById(topicId)).thenReturn(Optional.of(topic));
		when(userRepository.findById(userId)).thenReturn(Optional.of(user));

		// Act
		topicService.deleteTopic(topicId, userId);

		// Assert
		verify(topicRepository, times(1)).findById(topicId);
		verify(userRepository, times(1)).findById(userId);
		verify(topicRepository, times(1)).deleteById(topicId);
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
	void testCreateArgument_Success() {
		// Arrange
		String userId = "user123";
		Argument argument = new Argument();
		argument.setTopicId("topic456");
		argument.setCreatorId(userId);

		Topic topic = new Topic();
		topic.setId("topic456");

		User user = new User();
		user.setId(userId);
		user.setAdmin(false);
		user.setContributor(true);
		user.setModerator(false);

		when(topicRepository.findById("topic456")).thenReturn(Optional.of(topic));
		when(userRepository.findById("user123")).thenReturn(Optional.of(user));
		when(argumentRepository.save(argument)).thenReturn(argument);

		// Act
		Argument createdArgument = argumentService.createArgument(argument);

		// Assert
		assertEquals(argument, createdArgument);
		verify(topicRepository, times(1)).findById("topic456");
		verify(userRepository, times(1)).findById("user123");
		verify(argumentRepository, times(1)).save(argument);
	}

	@Test
	void testCreateArgument_TopicNotFound() {
		// Arrange
		Argument argument = new Argument();
		argument.setTopicId("nonexistentTopic");

		when(topicRepository.findById("nonexistentTopic")).thenReturn(Optional.empty());

		// Act & Assert
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			argumentService.createArgument(argument);
		});

		assertEquals("The associated topic does not exist.", exception.getMessage());
		verify(topicRepository, times(1)).findById("nonexistentTopic");
		verifyNoInteractions(userRepository);
		verifyNoInteractions(argumentRepository);
	}

	@Test
	void testUpdateArgument_Success() {
		// Arrange
		UpdateArgumentRequest request = new UpdateArgumentRequest();
		request.setArgumentId("arg1");
		request.setUserId("user123");
		request.setTitle("Updated Title");
		request.setDescription("Updated Description");

		Argument existingArgument = new Argument();
		existingArgument.setId("arg1");
		existingArgument.setCreatorId("user123");

		User user = new User();
		user.setId(request.getUserId());
		user.setAdmin(false);
		user.setContributor(true);
		user.setModerator(false);

		when(argumentRepository.findById("arg1")).thenReturn(Optional.of(existingArgument));
		when(userRepository.findById("user123")).thenReturn(Optional.of(user));
		when(argumentRepository.save(any(Argument.class))).thenAnswer(invocation -> invocation.getArgument(0));

		// Act
		argumentService.updateArgument(request);

		// Assert
		verify(argumentRepository, times(1)).findById("arg1");
		verify(userRepository, times(1)).findById("user123");
		verify(argumentRepository, times(1)).save(existingArgument);
		assertEquals("Updated Title", existingArgument.getTitle());
		assertEquals("Updated Description", existingArgument.getDescription());
	}

	@Test
	void testUpdateArgument_NotAuthorized() {
		// Arrange
		UpdateArgumentRequest request = new UpdateArgumentRequest();
		request.setArgumentId("arg1");
		request.setUserId("user123");

		Argument existingArgument = new Argument();
		existingArgument.setId("arg1");
		existingArgument.setCreatorId("creator456"); // Different creator

		User user = new User();
		user.setId(request.getUserId());
		user.setAdmin(false);
		user.setContributor(false);
		user.setModerator(false);

		when(argumentRepository.findById("arg1")).thenReturn(Optional.of(existingArgument));
		when(userRepository.findById("user123")).thenReturn(Optional.of(user));

		// Act & Assert
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			argumentService.updateArgument(request);
		});

		assertEquals("You are not authorized to update this argument.", exception.getMessage());
		verify(argumentRepository, times(1)).findById("arg1");
		verify(userRepository, times(1)).findById("user123");
		verifyNoMoreInteractions(argumentRepository);
	}

	@Test
	void testDeleteArgument_Success() {
		// Arrange
		String argumentId = "arg1";
		String userId = "user123";

		Argument argument = new Argument();
		argument.setId("arg1");
		argument.setCreatorId("user123");

		User user = new User();
		user.setId(userId);
		user.setAdmin(false);
		user.setContributor(false);
		user.setModerator(false);

		when(argumentRepository.findById(argumentId)).thenReturn(Optional.of(argument));
		when(userRepository.findById(userId)).thenReturn(Optional.of(user));

		// Act
		argumentService.deleteArgument(argumentId, userId);

		// Assert
		verify(argumentRepository, times(1)).findById(argumentId);
		verify(userRepository, times(1)).findById(userId);
		verify(argumentRepository, times(1)).deleteById(argumentId);
	}

	@Test
	void testDeleteArgument_NotAuthorized() {
		// Arrange
		String argumentId = "arg1";
		String userId = "user123";

		Argument argument = new Argument();
		argument.setId("arg1");
		argument.setCreatorId("creator456"); // Different creator

		User user = new User();
		user.setId("user123");
		user.setAdmin(false);
		user.setContributor(false);
		user.setModerator(false);

		when(argumentRepository.findById(argumentId)).thenReturn(Optional.of(argument));
		when(userRepository.findById(userId)).thenReturn(Optional.of(user));

		// Act & Assert
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			argumentService.deleteArgument(argumentId, userId);
		});

		assertEquals("You are not authorized to create an argument.", exception.getMessage());
		verify(argumentRepository, times(1)).findById(argumentId);
		verify(userRepository, times(1)).findById(userId);
		verifyNoMoreInteractions(argumentRepository);
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
	void testSubmitVote_CreateNewVote() {
		// Arrange
		Vote incomingVote = new Vote();
		incomingVote.setUserId("user123");
		incomingVote.setTopicId("topic456");
		incomingVote.setVoteType("up");

		when(voteRepository.findByUserIdAndTopicId("user123", "topic456")).thenReturn(Optional.empty());
		when(voteRepository.save(incomingVote)).thenReturn(incomingVote);

		// Act
		Vote result = voteService.submitVote(incomingVote);

		// Assert
		assertNotNull(result);
		assertEquals("up", result.getVoteType());
		verify(voteRepository, times(1)).findByUserIdAndTopicId("user123", "topic456");
		verify(voteRepository, times(1)).save(incomingVote);
	}

	// Test 2: Toggle off the vote when the same vote type is clicked
	@Test
	void testSubmitVote_ToggleOffVote() {
		// Arrange
		Vote existingVote = new Vote();
		existingVote.setId("vote123");
		existingVote.setUserId("user123");
		existingVote.setTopicId("topic456");
		existingVote.setVoteType("up");

		Vote incomingVote = new Vote();
		incomingVote.setUserId("user123");
		incomingVote.setTopicId("topic456");
		incomingVote.setVoteType("up");

		when(voteRepository.findByUserIdAndTopicId("user123", "topic456")).thenReturn(Optional.of(existingVote));

		// Act
		Vote result = voteService.submitVote(incomingVote);

		// Assert
		assertNull(result); // Vote was removed
		verify(voteRepository, times(1)).findByUserIdAndTopicId("user123", "topic456");
		verify(voteRepository, times(1)).deleteById("vote123");
	}

	// Test 3: Change the vote when a different vote type is submitted
	@Test
	void testSubmitVote_ChangeVoteType() {
		// Arrange
		Vote existingVote = new Vote();
		existingVote.setId("vote123");
		existingVote.setUserId("user123");
		existingVote.setTopicId("topic456");
		existingVote.setVoteType("up");

		Vote incomingVote = new Vote();
		incomingVote.setUserId("user123");
		incomingVote.setTopicId("topic456");
		incomingVote.setVoteType("down");

		when(voteRepository.findByUserIdAndTopicId("user123", "topic456")).thenReturn(Optional.of(existingVote));
		when(voteRepository.save(existingVote)).thenReturn(existingVote);

		// Act
		Vote result = voteService.submitVote(incomingVote);

		// Assert
		assertNotNull(result);
		assertEquals("down", result.getVoteType());
		verify(voteRepository, times(1)).findByUserIdAndTopicId("user123", "topic456");
		verify(voteRepository, times(1)).save(existingVote);
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
	void testDeleteArgumentVote() {
		argumentVoteService.deleteArgumentVote("1");
		verify(argumentVoteRepository, times(1)).deleteById("1");
	}
}
