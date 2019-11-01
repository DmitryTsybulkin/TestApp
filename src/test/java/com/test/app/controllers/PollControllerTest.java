package com.test.app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.app.dtos.PollDTO;
import com.test.app.entities.Poll;
import com.test.app.entities.Question;
import com.test.app.repositories.PollRepository;
import com.test.app.repositories.QuestionRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PollControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PollRepository pollRepository;
    @Autowired
    private QuestionRepository questionRepository;

    private Poll poll1;
    private Poll poll2;
    private Poll poll3;
    private Question question1;
    private Question question2;

    private DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd'T'HH:mm:ss.SSS").toFormatter();

    @Before
    public void setUp() throws Exception {
        questionRepository.deleteAll();
        pollRepository.deleteAll();
        poll1 = pollRepository.save(Poll.builder()
                .active(true)
                .name("Poll")
                .startDate(LocalDateTime.now().minusDays(2))
                .endDate(LocalDateTime.now().plusDays(2)).build());
        poll2 = pollRepository.save(Poll.builder()
                .active(true)
                .name("Poll_test")
                .startDate(LocalDateTime.now().minusDays(1))
                .endDate(LocalDateTime.now().plusDays(1)).build());
        poll3 = pollRepository.save(Poll.builder()
                .active(false)
                .name("Polly")
                .startDate(LocalDateTime.now().minusDays(3))
                .endDate(LocalDateTime.now().minusDays(1)).build());

        question1 = questionRepository.save(Question.builder().poll(poll1).sortOrder(1).text("txt").build());
        question2 = questionRepository.save(Question.builder().poll(poll1).sortOrder(2).text("test").build());
    }

    @After
    public void tearDown() throws Exception {
        questionRepository.deleteAll();
        pollRepository.deleteAll();
    }

    @Test
    public void createPoll() throws Exception {
        PollDTO dto = PollDTO.builder().active(true)
                .name("Testtt")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(1)).build();
        mockMvc.perform(post("/polls/create")
                .accept("application/json")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", notNullValue()))
                .andExpect(jsonPath("$.name", equalTo(dto.getName())))
                .andExpect(jsonPath("$.active", notNullValue()))
                .andExpect(jsonPath("$.active", equalTo(dto.getActive())))
                .andExpect(jsonPath("$.startDate", notNullValue()))
                .andExpect(jsonPath("$.startDate", equalTo(dto.getStartDate().format(dateTimeFormatter))))
                .andExpect(jsonPath("$.endDate", notNullValue()))
                .andExpect(jsonPath("$.endDate", equalTo(dto.getEndDate().format(dateTimeFormatter))));
    }

    @Test
    public void findPollById() throws Exception {
        mockMvc.perform(get("/polls/" + poll1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.id", equalTo(poll1.getId().intValue())))
                .andExpect(jsonPath("$.name", notNullValue()))
                .andExpect(jsonPath("$.name", equalTo(poll1.getName())))
                .andExpect(jsonPath("$.active", notNullValue()))
                .andExpect(jsonPath("$.active", equalTo(poll1.getActive())))
                .andExpect(jsonPath("$.startDate", notNullValue()))
                .andExpect(jsonPath("$.startDate", equalTo(poll1.getStartDate().format(dateTimeFormatter))))
                .andExpect(jsonPath("$.endDate", notNullValue()))
                .andExpect(jsonPath("$.endDate", equalTo(poll1.getEndDate().format(dateTimeFormatter))));
    }

    @Test
    public void findAllPolls() throws Exception {
        mockMvc.perform(get("/polls")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("pageable", objectMapper.writeValueAsString(PageRequest.of(0, 2).first())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.size", equalTo(2)))
                .andExpect(jsonPath("$.number", equalTo(2)))
                .andExpect(jsonPath("$.totalPages", equalTo(2)))
                .andExpect(jsonPath("$.numberOfElements", equalTo(2)))
                .andExpect(jsonPath("$.totalElements", equalTo(2)));
    }

    @Test
    public void findQuestionsByPoll() {
    }

    @Test
    public void updatePoll() throws Exception {
        PollDTO dto = PollDTO.builder().name("ae").active(false)
                .startDate(LocalDateTime.now().minusDays(5))
                .endDate(LocalDateTime.now().minusDays(1)).build();
        mockMvc.perform(patch("/polls/" + poll1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.id", equalTo(poll1.getId().intValue())))
                .andExpect(jsonPath("$.name", notNullValue()))
                .andExpect(jsonPath("$.name", equalTo(dto.getName())))
                .andExpect(jsonPath("$.active", notNullValue()))
                .andExpect(jsonPath("$.active", equalTo(dto.getActive())))
                .andExpect(jsonPath("$.startDate", notNullValue()))
                .andExpect(jsonPath("$.startDate", equalTo(dto.getStartDate().format(dateTimeFormatter))))
                .andExpect(jsonPath("$.endDate", notNullValue()))
                .andExpect(jsonPath("$.endDate", equalTo(dto.getEndDate().format(dateTimeFormatter))));
    }

    @Test
    public void deletePoll() throws Exception {
        mockMvc.perform(delete("/polls/" + poll1.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
        mockMvc.perform(get("/polls/" + poll1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", equalTo("NOT_FOUND")))
                .andExpect(jsonPath("$.description", startsWith("Опрос по id:")));
    }
}