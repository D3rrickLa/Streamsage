package com.laderrco.streamsage.Unit.ServiceTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.laderrco.streamsage.domains.SuggestionPackage;
import com.laderrco.streamsage.domains.Enums.Roles;
import com.laderrco.streamsage.dtos.FeedbackDTO;
import com.laderrco.streamsage.entities.Feedback;
import com.laderrco.streamsage.entities.User;
import com.laderrco.streamsage.repositories.FeedbackRepository;
import com.laderrco.streamsage.services.FeedbackServiceImpl;
import com.laderrco.streamsage.services.Interfaces.UserService;
import com.laderrco.streamsage.utils.TimestampGenerator;


// Testing the FeedbackService, not the acutal MVC controller
// need to fix this for user auth
public class FeedbackServiceTest {
    // @InjectMocks // need a real object, use Mock
    // @Mock // no presistent data
    
    @Mock
    private FeedbackRepository feedbackRepository;

    @Mock
    private UserService userService;

    @Mock
    private TimestampGenerator timestampGenerator;

    private FeedbackServiceImpl feedbackService;

    private User user;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        feedbackService = new FeedbackServiceImpl(feedbackRepository, userService, timestampGenerator);
        this.user = User.builder()
            .id(1L)
            .email("email@example.com")
            .password("null")
            .role(Roles.ROLE_USER)
            .build();
    }

    @Test
    public void testGetAll() {
        List<Feedback> feedbackList = feedbackService.findAll();
        assertTrue(feedbackList.size() == 0, "Size is wrong");


    }

    @Test
    public void testSubmitFeedback_Correct(){ 
        // // Create mock feedback list
        // List<Feedback> feedbackList = new ArrayList<>();
        // SuggestionPackage suggestionPackage = new SuggestionPackage();
        // Feedback feedback = new Feedback(
        //     "This is a test comment", 5, suggestionPackage
        //     );
        
        // // Simulate saving by modifying the mock behavior
        // doAnswer(invocation -> {
        //     feedbackList.add(feedback);
        //     return null;
        // }).when(feedbackService).save(feedback);

        // when(feedbackService.findById(0L)).thenReturn(Optional.of(feedback)); // Mock retrieval
        
        // feedbackService.save(feedback);
        // assertEquals(feedback, feedbackService.findById(0L).get());

        when(userService.findIdByEmail()).thenReturn(user.getId());
        when(userService.findById(user.getId())).thenReturn(Optional.of(user)); // Ensures a value is present 

        FeedbackDTO feedbackDTO = new FeedbackDTO();
        feedbackDTO.setComment("some comment");
        feedbackDTO.setRating(5);

        Feedback feedbackTest2= new Feedback();
        feedbackTest2.setComment("some comment");
        feedbackTest2.setRating(5);
        feedbackTest2.setUser(user);

        when(feedbackRepository.save(any())).thenReturn(feedbackTest2);

        Feedback feedbackTest = feedbackService.submitFeedback(feedbackDTO, new SuggestionPackage());

        assertEquals(feedbackTest.getUser(), user);
        assertEquals(feedbackTest2, feedbackTest);
    }

    @Test
    void testFindById_Correct() {
        when(feedbackRepository.findById(anyLong())).thenReturn(Optional.of(new Feedback()));

        Optional<Feedback> testFeedback = feedbackRepository.findById(1L);
        
        assertEquals(testFeedback.get(), new Feedback());
    }


    @Test
    void testDeleteByUserID() {
        // feedback has a user, we delete feeback by the user id;
        doNothing().when(feedbackRepository).deleteByUserId(anyLong());    

        feedbackService.deleteByUserId(1L);


        verify(feedbackRepository, times(1)).deleteByUserId(1L);
    }

    @Test
    void testFindById() {
        when(feedbackRepository.findById(anyLong())).thenReturn(Optional.of(new Feedback()));

        Feedback feedbackTest = feedbackService.findById(1L).get();

        assertEquals(feedbackTest, new Feedback());
    }
}
