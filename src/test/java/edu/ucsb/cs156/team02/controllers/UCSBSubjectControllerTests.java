package edu.ucsb.cs156.team02.controllers;

import edu.ucsb.cs156.team02.repositories.UserRepository;
import edu.ucsb.cs156.team02.testconfig.TestConfig;
import edu.ucsb.cs156.team02.ControllerTestCase;
import edu.ucsb.cs156.team02.entities.UCSBSubject;
import edu.ucsb.cs156.team02.repositories.UCSBSubjectRepository;
import edu.ucsb.cs156.team02.controllers.UCSBSubjectController;

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

@WebMvcTest(controllers = UCSBSubjectController.class)
@Import(TestConfig.class)

public class UCSBSubjectControllerTests extends ControllerTestCase{
    @MockBean
    UCSBSubjectRepository ucsbSubjectRepository;

    @MockBean
    UserRepository userRepository;

    // Test /all endpoint 
    @Test
    public void api_all() throws Exception{

        // arrange
        UCSBSubject subject1 = UCSBSubject.builder().id(1L).subjectCode("code 1").subjectTranslation("translation 1").deptCode("dept code 1").collegeCode("college code 1").relatedDeptCode("related dept code 1").inactive(true).build();
        UCSBSubject subject2 = UCSBSubject.builder().id(2L).subjectCode("code 2").subjectTranslation("translation 2").deptCode("dept code 2").collegeCode("college code 2").relatedDeptCode("related dept code 2").inactive(true).build();
        UCSBSubject subject3 = UCSBSubject.builder().id(3L).subjectCode("code 3").subjectTranslation("translation 3").deptCode("dept code 3").collegeCode("college code 3").relatedDeptCode("related dept code 3").inactive(true).build();

        ArrayList<UCSBSubject> expectedUCSBSubject = new ArrayList<>();
        expectedUCSBSubject.addAll(Arrays.asList(subject1, subject2, subject3));

        when(ucsbSubjectRepository.findAll()).thenReturn(expectedUCSBSubject);

        // act
        MvcResult response = mockMvc.perform(get("/api/UCSBSubject/all"))
                .andExpect(status().isOk()).andReturn();

        // assert

        verify(ucsbSubjectRepository, times(1)).findAll();
        String expectedJson = mapper.writeValueAsString(expectedUCSBSubject);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }

    @Test
    public void api_all_with_no_subjects() throws Exception {

        // arrange
        ArrayList<UCSBSubject> expectedUCSBSubject = new ArrayList<>();

        when(ucsbSubjectRepository.findAll()).thenReturn(expectedUCSBSubject);

        // act
        MvcResult response = mockMvc.perform(get("/api/UCSBSubject/all"))
                .andExpect(status().isOk()).andReturn();

        // assert
        verify(ucsbSubjectRepository, times(1)).findAll();
        String expectedJson = mapper.writeValueAsString(expectedUCSBSubject);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }

    // Test /post endpoint
    @Test
    public void api_post() throws Exception {
        // arrange
        UCSBSubject expectedUCSBSubject = UCSBSubject.builder().id(1L).subjectCode("code 1").subjectTranslation("translation 1").deptCode("dept code 1").collegeCode("college code 1").relatedDeptCode("related dept code 1").inactive(true).build();

        when(ucsbSubjectRepository.save(eq(expectedUCSBSubject))).thenReturn(expectedUCSBSubject);

        // act
        MvcResult response = mockMvc.perform(
                post("/api/UCSBSubject/post?id=1&subjectCode=code 1&subjectTranslation=translation 1&deptCode=dept code 1&collegeCode=college code 1&relatedDeptCode=related dept code 1&inactive=true")
                        .with(csrf()))
                .andExpect(status().isOk()).andReturn();

        // assert
        verify(ucsbSubjectRepository, times(1)).save(expectedUCSBSubject);
        String expectedJson = mapper.writeValueAsString(expectedUCSBSubject);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }


    // //Test api /get with id parameter endpoint
    // @Test
    // public void api_get_id_returns_a_subject_that_exists() throws Exception {

    //     // arrange
    //     UCSBSubject expectedUCSBSubject = UCSBSubject.builder().id(1L).subjectCode("code 1").subjectTranslation("translation 1").deptCode("dept code 1").collegeCode("college code 1").relatedDeptCode("related dept code 1").inactive(true).build();
    //     when(ucsbSubjectRepository.findById(eq(1L))).thenReturn(Optional.of(expectedUCSBSubject));

    //     // act
    //     MvcResult response = mockMvc.perform(get("/api/UCSBSubject?id=1"))
    //             .andExpect(status().isOk()).andReturn();

    //     // assert

    //     verify(ucsbSubjectRepository, times(1)).findById(eq(1L));
    //     String expectedJson = mapper.writeValueAsString(expectedUCSBSubject);
    //     String responseString = response.getResponse().getContentAsString();
    //     assertEquals(expectedJson, responseString);
    // }

    // @Test
    // public void api_get_id_returns_a_subject_that_does_not_exist() throws Exception {

    //     // arrange
    //     when(ucsbSubjectRepository.findById(eq(7L))).thenReturn(Optional.empty());

    //     // act
    //     MvcResult response = mockMvc.perform(get("/api/UCSBSubject?id=7"))
    //             .andExpect(status().isBadRequest()).andReturn();

    //     // assert

    //     verify(ucsbSubjectRepository, times(1)).findById(eq(7L));
    //     String responseString = response.getResponse().getContentAsString();
    //     assertEquals("UCSBSubject with id 7 not found", responseString);
    // }
    // @WithMockUser(roles = { "USER" })
    // @Test
    // public void api_subject_put_subject() throws Exception {
    //     // arrange

    //     Todo todo1 = Todo.builder().title("Todo 1").details("Todo 1").done(false).user(u).id(67L).build();
    //     // We deliberately set the user information to another user
    //     // This shoudl get ignored and overwritten with currrent user when todo is saved

    //     Todo updatedTodo = Todo.builder().title("New Title").details("New Details").done(true).user(otherUser).id(67L).build();
    //     Todo correctTodo = Todo.builder().title("New Title").details("New Details").done(true).user(u).id(67L).build();

    //     String requestBody = mapper.writeValueAsString(updatedTodo);
    //     String expectedReturn = mapper.writeValueAsString(correctTodo);

    //     when(todoRepository.findById(eq(67L))).thenReturn(Optional.of(todo1));

    //     // act
    //     MvcResult response = mockMvc.perform(
    //             put("/api/todos?id=67")
    //                     .contentType(MediaType.APPLICATION_JSON)
    //                     .characterEncoding("utf-8")
    //                     .content(requestBody)
    //                     .with(csrf()))
    //             .andExpect(status().isOk()).andReturn();

    //     // assert
    //     verify(todoRepository, times(1)).findById(67L);
    //     verify(todoRepository, times(1)).save(correctTodo); // should be saved with correct user
    //     String responseString = response.getResponse().getContentAsString();
    //     assertEquals(expectedReturn, responseString);
    // }

    // @WithMockUser(roles = { "USER" })
    // @Test
    // public void api_todos__user_logged_in__cannot_put_todo_that_does_not_exist() throws Exception {
    //     // arrange

    //     Todo updatedTodo = Todo.builder().title("New Title").details("New Details").done(true).id(67L).build();

    //     String requestBody = mapper.writeValueAsString(updatedTodo);

    //     when(todoRepository.findById(eq(67L))).thenReturn(Optional.empty());

    //     // act
    //     MvcResult response = mockMvc.perform(
    //             put("/api/todos?id=67")
    //                     .contentType(MediaType.APPLICATION_JSON)
    //                     .characterEncoding("utf-8")
    //                     .content(requestBody)
    //                     .with(csrf()))
    //             .andExpect(status().isBadRequest()).andReturn();

    //     // assert
    //     verify(todoRepository, times(1)).findById(67L);
    //     String responseString = response.getResponse().getContentAsString();
    //     assertEquals("todo with id 67 not found", responseString);
    // }

}