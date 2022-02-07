package edu.ucsb.cs156.team02.controllers;

import edu.ucsb.cs156.team02.repositories.UserRepository;
import edu.ucsb.cs156.team02.testconfig.TestConfig;
import edu.ucsb.cs156.team02.ControllerTestCase;
import edu.ucsb.cs156.team02.entities.UCSBRequirement;
import edu.ucsb.cs156.team02.repositories.UCSBRequirementRepository;
import edu.ucsb.cs156.team02.controllers.UCSBRequirementController;

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

@WebMvcTest(controllers = UCSBRequirementController.class)
@Import(TestConfig.class)
public class UCSBRequirementControllerTests extends ControllerTestCase {

    @MockBean
    UCSBRequirementRepository UCSBRequirementRepository;

    @MockBean
    UserRepository userRepository;

    // Test /all endpoint

    @Test
    public void api_all() throws Exception {

        // arrange

        UCSBRequirement UCSBRequirement1 = UCSBRequirement.builder().requirementCode("test")
            .requirementTranslation("teast")
            .collegeCode("tesat")
            .objCode("teast")
            .courseCount(1)
            .units(1)
            .inactive(false).build();
        UCSBRequirement UCSBRequirement2 = UCSBRequirement.builder().requirementCode("test")
            .requirementTranslation("terst")
            .collegeCode("trest")
            .objCode("terst")
            .courseCount(1)
            .units(1)
            .inactive(false).build();
        UCSBRequirement UCSBRequirement3 = UCSBRequirement.builder().requirementCode("test")
            .requirementTranslation("tyest")
            .collegeCode("teyst")
            .objCode("tyest")
            .courseCount(1)
            .units(1)
            .inactive(false).build();

        ArrayList<UCSBRequirement> expectedUCSBRequirements = new ArrayList<>();
        expectedUCSBRequirements.addAll(Arrays.asList(UCSBRequirement1, UCSBRequirement2, UCSBRequirement3));

        when(UCSBRequirementRepository.findAll()).thenReturn(expectedUCSBRequirements);

        // act
        MvcResult response = mockMvc.perform(get("/api/UCSBRequirements/all"))
                .andExpect(status().isOk()).andReturn();

        // assert

        verify(UCSBRequirementRepository, times(1)).findAll();
        String expectedJson = mapper.writeValueAsString(expectedUCSBRequirements);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }

    @Test
    public void api_all_with_no_subreddits() throws Exception {

        // arrange
        ArrayList<UCSBRequirement> expectedUCSBRequirements = new ArrayList<>();

        when(UCSBRequirementRepository.findAll()).thenReturn(expectedUCSBRequirements);

        // act
        MvcResult response = mockMvc.perform(get("/api/UCSBRequirements/all"))
                .andExpect(status().isOk()).andReturn();

        // assert
        verify(UCSBRequirementRepository, times(1)).findAll();
        String expectedJson = mapper.writeValueAsString(expectedUCSBRequirements);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }

    // Test /post endpoint

    @Test
    public void api_post() throws Exception {
        // arrange

        UCSBRequirement expectedUCSBRequirement = UCSBRequirement.builder().requirementCode("test")
            .requirementTranslation("test")
            .collegeCode("test")
            .objCode("test")
            .courseCount(1)
            .units(1)
            .inactive(false).build();

        when(UCSBRequirementRepository.save(eq(expectedUCSBRequirement))).thenReturn(expectedUCSBRequirement);


        
        // act
        MvcResult response = mockMvc.perform(
                post("/api/UCSBRequirements/post?requirementCode=test&requirementTranslation=test&collegeCode=test&objCode=test&courseCount=1&units=1&inactive=false")
                        .with(csrf()))
                .andExpect(status().isOk()).andReturn();

        // assert
        verify(UCSBRequirementRepository, times(1)).save(expectedUCSBRequirement);
        String expectedJson = mapper.writeValueAsString(expectedUCSBRequirement);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }

}
