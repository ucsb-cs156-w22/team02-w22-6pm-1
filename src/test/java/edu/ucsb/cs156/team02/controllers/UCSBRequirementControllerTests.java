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
    public void api_all_with_no_requirements() throws Exception {

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
                .inactive(true).build();

        when(UCSBRequirementRepository.save(eq(expectedUCSBRequirement))).thenReturn(expectedUCSBRequirement);

        // act
        MvcResult response = mockMvc.perform(
                post("/api/UCSBRequirements/post?requirementCode=test&requirementTranslation=test&collegeCode=test&objCode=test&courseCount=1&units=1&inactive=true")
                        .with(csrf()))
                .andExpect(status().isOk()).andReturn();

        // assert
        verify(UCSBRequirementRepository, times(1)).save(expectedUCSBRequirement);
        String expectedJson = mapper.writeValueAsString(expectedUCSBRequirement);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }

    // Test api /get with id parameter endpoint
    @Test
    public void api_get_id_returns_a_requirement_that_exists() throws Exception {

        // arrange
        UCSBRequirement expectedUCSBRequirement = UCSBRequirement.builder().requirementCode("test")
                .requirementTranslation("test")
                .collegeCode("test")
                .objCode("test")
                .courseCount(1)
                .units(1)
                .inactive(false).build();

        when(UCSBRequirementRepository.findById(eq(1L))).thenReturn(Optional.of(expectedUCSBRequirement));

        // act
        MvcResult response = mockMvc.perform(get("/api/UCSBRequirements?id=1"))
                .andExpect(status().isOk()).andReturn();

        // assert

        verify(UCSBRequirementRepository, times(1)).findById(eq(1L));
        String expectedJson = mapper.writeValueAsString(expectedUCSBRequirement);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }

    // Test api /get UCSB Requirement id that doesn't exist
    @Test
    public void api_get_id_returns_a_requirement_that_does_not_exist() throws Exception {

        // arrange
        when(UCSBRequirementRepository.findById(eq(7L))).thenReturn(Optional.empty());

        // act
        MvcResult response = mockMvc.perform(get("/api/UCSBRequirements?id=7"))
                .andExpect(status().isBadRequest()).andReturn();

        // assert

        verify(UCSBRequirementRepository, times(1)).findById(eq(7L));
        String responseString = response.getResponse().getContentAsString();
        assertEquals("id 7 not found", responseString);
    }

    // Test api /put requirement given id
    @Test
    public void api_requirement_put_requirement() throws Exception {
            // arrange

            UCSBRequirement UCSBRequirement1 = UCSBRequirement.builder().requirementCode("test1")
            .requirementTranslation("teast1")
            .collegeCode("tesat1")
            .objCode("teast1")
            .courseCount(1)
            .units(1)
            .inactive(false).build();

            UCSBRequirement updatedUCSBRequirement = UCSBRequirement.builder().requirementCode("test")
            .requirementTranslation("teast")
            .collegeCode("tesat")
            .objCode("teast")
            .courseCount(1)
            .units(1)
            .inactive(false).build();

            UCSBRequirement correctUCSBRequirement = UCSBRequirement.builder().requirementCode("test")
            .requirementTranslation("teast")
            .collegeCode("tesat")
            .objCode("teast")
            .courseCount(1)
            .units(1)
            .inactive(false).build();

            String requestBody = mapper.writeValueAsString(updatedUCSBRequirement);
            String expectedReturn = mapper.writeValueAsString(correctUCSBRequirement);

            when(UCSBRequirementRepository.findById(eq(1L))).thenReturn(Optional.of(UCSBRequirement1));

            // act
            MvcResult response = mockMvc.perform(
                            put("/api/UCSBRequirements?id=1")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .characterEncoding("utf-8")
                                            .content(requestBody)
                                            .with(csrf()))
                            .andExpect(status().isOk()).andReturn();

            // assert
            verify(UCSBRequirementRepository, times(1)).findById(1L);
            verify(UCSBRequirementRepository, times(1)).save(correctUCSBRequirement);
            String responseString = response.getResponse().getContentAsString();
            assertEquals(expectedReturn, responseString);
    }

    // Test api /put with requirement id that doesn't exist
    @Test
    public void api_requirement_put_that_does_not_exist() throws Exception {
            // arrange

            UCSBRequirement UCSBRequirement1 = UCSBRequirement.builder().requirementCode("test1")
            .requirementTranslation("teast1")
            .collegeCode("tesat1")
            .objCode("teast1")
            .courseCount(1)
            .units(1)
            .inactive(true).build();

            String requestBody = mapper.writeValueAsString(UCSBRequirement1);

            when(UCSBRequirementRepository.findById(eq(1L))).thenReturn(Optional.empty());

            // act
            MvcResult response = mockMvc.perform(
                            put("/api/UCSBRequirements?id=1")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .characterEncoding("utf-8")
                                            .content(requestBody)
                                            .with(csrf()))
                            .andExpect(status().isBadRequest()).andReturn();

            // assert
            verify(UCSBRequirementRepository, times(1)).findById(1L);
            String responseString = response.getResponse().getContentAsString();
            assertEquals("id 1 not found", responseString);
    }

}
