package com.test.app.controllers;

import com.test.app.dtos.QuestionDTO;
import com.test.app.entities.Question;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@ContextConfiguration(classes = QuestionController.class)
@WebMvcTest
@RunWith(SpringRunner.class)
public class QuestionControllerTest {


    private final QuestionDTO questionDTO = QuestionDTO.builder().text("text").sortOrder(1).pollId(1L).build();

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void createQuestion() {

    }

    @Test
    public void findQuestionById() {
    }

    @Test
    public void findAllQuestions() {
    }

    @Test
    public void updateQuestion() {
    }

    @Test
    public void deleteQuestion() {
    }
}