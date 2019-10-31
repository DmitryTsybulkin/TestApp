package com.test.app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.app.dtos.PollDTO;
import com.test.app.representatons.PollRepresentation;
import com.test.app.representatons.QuestionRepresentation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class PollControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private PollRepresentation pollRepresentation;
    @Mock
    private QuestionRepresentation questionRepresentation;

    private PollController pollController;

    private PollDTO pollDTO = PollDTO.builder()
            .id(1L)
            .name("poll")
            .startDate(LocalDateTime.now().minusDays(2))
            .endDate(LocalDateTime.now().plusDays(2))
            .active(true)
            .build();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        pollController = new PollController(pollRepresentation, questionRepresentation);
    }

    @Test
    public void createPoll() throws Exception {
        Mockito.when(pollRepresentation.createPoll(Mockito.any(PollDTO.class))).thenReturn(pollDTO);
        mockMvc.perform(post("/polls/create")
                .content(objectMapper.writeValueAsString(PollDTO.builder()
                        .active(pollDTO.getActive())
                        .endDate(pollDTO.getEndDate())
                        .startDate(pollDTO.getStartDate())
                        .name(pollDTO.getName()).build())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(request().asyncStarted())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.id", equalTo(pollDTO.getId())))
                .andExpect(jsonPath("$.name", notNullValue()))
                .andExpect(jsonPath("$.name", equalTo(pollDTO.getName())))
                .andExpect(jsonPath("$.active", notNullValue()))
                .andExpect(jsonPath("$.active", equalTo(pollDTO.getActive())))
                .andExpect(jsonPath("$.startDate", notNullValue()))
                .andExpect(jsonPath("$.startDate", equalTo(pollDTO.getStartDate())))
                .andExpect(jsonPath("$.endDate", notNullValue()))
                .andExpect(jsonPath("$.endDate", equalTo(pollDTO.getEndDate())));
    }

    @Test
    public void findPollById() {
    }

    @Test
    public void findAllPolls() {
    }

    @Test
    public void findQuestionsByPoll() {
    }

    @Test
    public void updatePoll() {
    }

    @Test
    public void deletePoll() {
    }
}