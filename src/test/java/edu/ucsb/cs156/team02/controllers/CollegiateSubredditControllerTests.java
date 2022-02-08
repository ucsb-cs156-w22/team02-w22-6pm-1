package edu.ucsb.cs156.team02.controllers;

import edu.ucsb.cs156.team02.repositories.UserRepository;
import edu.ucsb.cs156.team02.testconfig.TestConfig;
import edu.ucsb.cs156.team02.ControllerTestCase;
import edu.ucsb.cs156.team02.entities.CollegiateSubreddit;
import edu.ucsb.cs156.team02.repositories.CollegiateSubredditRepository;
import edu.ucsb.cs156.team02.controllers.CollegiateSubredditController;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = CollegiateSubredditController.class)
@Import(TestConfig.class)
public class CollegiateSubredditControllerTests extends ControllerTestCase {

    @MockBean
    CollegiateSubredditRepository collegiateSubredditRepository;

    @MockBean
    UserRepository userRepository;

    // Test /all endpoint

    @Test
    public void api_all() throws Exception {

        // arrange

        CollegiateSubreddit collegiateSubreddit1 = CollegiateSubreddit.builder().name("College 1").location("Iceland").subreddit("icelandu").id(1L).build();
        CollegiateSubreddit collegiateSubreddit2 = CollegiateSubreddit.builder().name("College 2").location("Czech Republic").subreddit("czechu").id(2L).build();
        CollegiateSubreddit collegiateSubreddit3 = CollegiateSubreddit.builder().name("College 3").location("Norway").subreddit("Norwayu").id(3L).build();

        ArrayList<CollegiateSubreddit> expectedCollegiateSubreddits = new ArrayList<>();
        expectedCollegiateSubreddits.addAll(Arrays.asList(collegiateSubreddit1, collegiateSubreddit2, collegiateSubreddit3));

        when(collegiateSubredditRepository.findAll()).thenReturn(expectedCollegiateSubreddits);

        // act
        MvcResult response = mockMvc.perform(get("/api/collegiateSubreddits/all"))
                .andExpect(status().isOk()).andReturn();

        // assert

        verify(collegiateSubredditRepository, times(1)).findAll();
        String expectedJson = mapper.writeValueAsString(expectedCollegiateSubreddits);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }

    @Test
    public void api_all_with_no_subreddits() throws Exception {

        // arrange
        ArrayList<CollegiateSubreddit> expectedCollegiateSubreddits = new ArrayList<>();

        when(collegiateSubredditRepository.findAll()).thenReturn(expectedCollegiateSubreddits);

        // act
        MvcResult response = mockMvc.perform(get("/api/collegiateSubreddits/all"))
                .andExpect(status().isOk()).andReturn();

        // assert
        verify(collegiateSubredditRepository, times(1)).findAll();
        String expectedJson = mapper.writeValueAsString(expectedCollegiateSubreddits);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }

    // Test /post endpoint

    @Test
    public void api_post() throws Exception {
        // arrange

        CollegiateSubreddit expectedCollegiateSubreddit = CollegiateSubreddit.builder().name("College 1").location("Iceland").subreddit("icelandu").id(0L).build();

        when(collegiateSubredditRepository.save(eq(expectedCollegiateSubreddit))).thenReturn(expectedCollegiateSubreddit);

        // act
        MvcResult response = mockMvc.perform(
                post("/api/collegiateSubreddits/post?name=College 1&location=Iceland&subreddit=icelandu")
                        .with(csrf()))
                .andExpect(status().isOk()).andReturn();

        // assert
        verify(collegiateSubredditRepository, times(1)).save(expectedCollegiateSubreddit);
        String expectedJson = mapper.writeValueAsString(expectedCollegiateSubreddit);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }

    //Test api /get with id parameter endpoint
    @Test
    public void api_get_id_returns_a_subreddit_that_exists() throws Exception {

        // arrange
        CollegiateSubreddit expectedCollegiateSubreddit = CollegiateSubreddit.builder().name("College 1").location("Iceland").subreddit("icelandu").id(1L).build();
        when(collegiateSubredditRepository.findById(eq(1L))).thenReturn(Optional.of(expectedCollegiateSubreddit));

        // act
        MvcResult response = mockMvc.perform(get("/api/collegiateSubreddits?id=1"))
                .andExpect(status().isOk()).andReturn();

        // assert

        verify(collegiateSubredditRepository, times(1)).findById(eq(1L));
        String expectedJson = mapper.writeValueAsString(expectedCollegiateSubreddit);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }

    //Test api /get Collegiate Subreddit id that doesn't exist
    @Test
    public void api_get_id_returns_a_subreddit_that_does_not_exist() throws Exception {

        // arrange
        when(collegiateSubredditRepository.findById(eq(7L))).thenReturn(Optional.empty());

        // act
        MvcResult response = mockMvc.perform(get("/api/collegiateSubreddits?id=7"))
                .andExpect(status().isBadRequest()).andReturn();

        // assert

        verify(collegiateSubredditRepository, times(1)).findById(eq(7L));
        String responseString = response.getResponse().getContentAsString();
        assertEquals("Collegiate Subreddit with id 7 not found", responseString);
    }
}
