package com.test.app.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.app.dtos.QuestionDTO;
import com.test.app.entities.Poll;
import com.test.app.entities.Question;
import com.test.app.repositories.PollRepository;
import com.test.app.repositories.QuestionRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;

import static com.test.app.TestUtils.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class QuestionControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PollRepository pollRepository;
    @Autowired
    private QuestionRepository questionRepository;

    private Poll poll;
    private Question question;

    @Before
    public void setUp() throws Exception {
        questionRepository.deleteAll();
        pollRepository.deleteAll();
        poll = pollRepository.save(stubPoll1());
        question = questionRepository.save(stubQuestion1(poll));
        questionRepository.save(stubQuestion2(poll));
    }

    @After
    public void tearDown() throws Exception {
        questionRepository.deleteAll();
        pollRepository.deleteAll();
    }

    @Test
    public void createQuestion() throws Exception {
        QuestionDTO dto = QuestionDTO.builder().pollId(poll.getId())
                .sortOrder(3)
                .text("simple_text").build();
        mockMvc.perform(post("/questions/create")
                .accept("application/json")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.pollId", notNullValue()))
                .andExpect(jsonPath("$.pollId", equalTo(dto.getPollId().intValue())))
                .andExpect(jsonPath("$.text", notNullValue()))
                .andExpect(jsonPath("$.text", equalTo(dto.getText())))
                .andExpect(jsonPath("$.sortOrder", notNullValue()))
                .andExpect(jsonPath("$.sortOrder", equalTo(dto.getSortOrder())));
    }

    @Test
    public void findQuestionById() throws Exception {
        mockMvc.perform(get("/questions/" + question.getId())
                .accept("application/json")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.id", equalTo(question.getId().intValue())))
                .andExpect(jsonPath("$.pollId", notNullValue()))
                .andExpect(jsonPath("$.pollId", equalTo(question.getPoll().getId().intValue())))
                .andExpect(jsonPath("$.text", notNullValue()))
                .andExpect(jsonPath("$.text", equalTo(question.getText())))
                .andExpect(jsonPath("$.sortOrder", notNullValue()))
                .andExpect(jsonPath("$.sortOrder", equalTo(question.getSortOrder())));
    }

    @Test
    public void findAllQuestions() throws Exception {
        List<QuestionDTO> questionDTOS = objectMapper.readValue(mockMvc.perform(get("/questions")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString(), new TypeReference<List<QuestionDTO>>() {
        });
        assertEquals(2, questionDTOS.size());
    }

    @Test
    public void updateQuestion() throws Exception {
        QuestionDTO dto = QuestionDTO.builder().pollId(poll.getId()).sortOrder(12).text("new_text").build();
        mockMvc.perform(patch("/questions/" + question.getId())
                .accept("application/json")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.id", equalTo(question.getId().intValue())))
                .andExpect(jsonPath("$.pollId", notNullValue()))
                .andExpect(jsonPath("$.pollId", equalTo(dto.getPollId().intValue())))
                .andExpect(jsonPath("$.text", notNullValue()))
                .andExpect(jsonPath("$.text", equalTo(dto.getText())))
                .andExpect(jsonPath("$.sortOrder", notNullValue()))
                .andExpect(jsonPath("$.sortOrder", equalTo(dto.getSortOrder())));
    }

    @Test
    public void deleteQuestion() throws Exception {
        mockMvc.perform(delete("/questions/" + question.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
        mockMvc.perform(get("/questions/" + question.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", equalTo("NOT_FOUND")))
                .andExpect(jsonPath("$.description", startsWith("Вопрос по id:")));
    }
}