package com.test.app.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.app.dtos.PollDTO;
import com.test.app.dtos.QuestionDTO;
import com.test.app.entities.Poll;
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

import java.time.LocalDateTime;
import java.util.List;

import static com.test.app.TestUtils.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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
    private Poll poll3;

    @Before
    public void setUp() throws Exception {
        questionRepository.deleteAll();
        pollRepository.deleteAll();
        poll1 = pollRepository.save(stubPoll1());
        pollRepository.save(stubPoll2());
        poll3 = pollRepository.save(stubPoll3());

        questionRepository.save(stubQuestion1(poll1));
        questionRepository.save(stubQuestion2(poll1));
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
                .andExpect(jsonPath("$.startDate", equalTo(parseDate(dto.getStartDate()))))
                .andExpect(jsonPath("$.endDate", notNullValue()))
                .andExpect(jsonPath("$.endDate", equalTo(parseDate(dto.getEndDate()))));
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
                .andExpect(jsonPath("$.startDate", equalTo(parseDate(poll1.getStartDate()))))
                .andExpect(jsonPath("$.endDate", notNullValue()))
                .andExpect(jsonPath("$.endDate", equalTo(parseDate(poll1.getEndDate()))));
    }

    @Test
    public void findAllPolls() throws Exception {
        List<PollDTO> pollDTOS = objectMapper.readValue(mockMvc.perform(get("/polls")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString(), new TypeReference<List<PollDTO>>() {
        });
        assertEquals(3, pollDTOS.size());

        pollDTOS = objectMapper.readValue(mockMvc.perform(get("/polls")
                .param("name", poll3.getName())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString(), new TypeReference<List<PollDTO>>() {
        });
        assertEquals(1, pollDTOS.size());

        pollDTOS = objectMapper.readValue(mockMvc.perform(get("/polls")
                .param("active", String.valueOf(poll3.getActive()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString(), new TypeReference<List<PollDTO>>() {
        });
        assertEquals(1, pollDTOS.size());

        pollDTOS = objectMapper.readValue(mockMvc.perform(get("/polls")
                .param("startDate", parseDate(LocalDateTime.now().minusDays(3)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString(), new TypeReference<List<PollDTO>>() {
        });
        assertEquals(2, pollDTOS.size());
        assertTrue(pollDTOS.stream().anyMatch(dto -> dto.getName().equals("Poll") || dto.getName().equals("Poll_test")));

        pollDTOS = objectMapper.readValue(mockMvc.perform(get("/polls")
                .param("endDate", parseDate(LocalDateTime.now()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString(), new TypeReference<List<PollDTO>>() {
        });
        assertEquals(1, pollDTOS.size());
        assertTrue(pollDTOS.stream().anyMatch(dto -> dto.getName().equals("Polly")));
    }

    @Test
    public void findQuestionsByPoll() throws Exception {
        List<QuestionDTO> questionDTOS = objectMapper.readValue(mockMvc
                .perform(get("/polls/" + poll1.getId() + "/questions")
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
                .andExpect(jsonPath("$.startDate", equalTo(parseDate(dto.getStartDate()))))
                .andExpect(jsonPath("$.endDate", notNullValue()))
                .andExpect(jsonPath("$.endDate", equalTo(parseDate(dto.getEndDate()))));
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