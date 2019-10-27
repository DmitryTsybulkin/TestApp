package com.test.app.services;

import com.test.app.entities.Poll;
import com.test.app.entities.Question;
import com.test.app.excpetions.EntryDuplicateException;
import com.test.app.excpetions.ResourceNotFoundException;
import com.test.app.repositories.PollRepository;
import com.test.app.repositories.QuestionRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Transactional
@SpringBootTest
@RunWith(SpringRunner.class)
public class QuestionServiceTest {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private PollRepository pollRepository;

    private Poll poll;
    private Question question;

    @Before
    public void setUp() {
        questionRepository.deleteAll();
        pollRepository.deleteAll();
        poll = pollRepository.save(Poll.builder()
                .active(true)
                .name("Опрос \"Поиск работы\"")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(5))
                .build());
        question = questionService.create(Question.builder()
                .text("Пользуетесь ли вы интрнетом?")
                .sortOrder(1)
                .poll(poll)
                .build());
    }

    @After
    public void tearDown() {
        questionRepository.deleteAll();
        pollRepository.deleteAll();
    }

    @Test
    public void create() {
        Question result = questionService.create(Question.builder()
                .text("Какими интернет ресурсвами вы предпочитаете пользоваться для поиска работы?")
                .sortOrder(2)
                .poll(poll)
                .build());
        assertNotNull(result);
        assertNotNull(result.getId());
    }

    @Test(expected = EntryDuplicateException.class)
    public void createFailedEntryDuplicateExceptionText() {
        questionService.create(Question.builder()
                .text("Пользуетесь ли вы интрнетом?")
                .sortOrder(2)
                .poll(poll)
                .build());
    }

    @Test(expected = EntryDuplicateException.class)
    public void createFailedEntryDuplicateExceptionOrder() {
        questionService.create(Question.builder()
                .text("Какими интернет ресурсвами вы предпочитаете пользоваться для поиска работы?")
                .sortOrder(1)
                .poll(poll)
                .build());
    }

    @Test
    public void findById() {
        Question result = questionService.findById(question.getId());
        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getPoll());
        assertNotNull(result.getText());
        assertNotNull(result.getSortOrder());
        assertEquals(question.getId(), result.getId());
        assertEquals(question.getText(), result.getText());
        assertEquals(question.getSortOrder(), result.getSortOrder());
        assertEquals(question.getPoll(), result.getPoll());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void findByIdFailedNotFound() {
        questionService.findById(1000L);
    }

    @Test
    public void findAll() {
        Page<Question> all = questionService.findAll(Pageable.unpaged());
        assertNotNull(all);
        assertEquals(1, all.getNumberOfElements());
    }

    @Test
    public void update() {
        Question target = Question.builder()
                .id(question.getId())
                .poll(poll)
                .sortOrder(2)
                .text("new Text")
                .build();
        Question updated = questionService.update(target);
        assertNotNull(updated);
        assertNotNull(updated.getId());
        assertNotNull(updated.getPoll());
        assertNotNull(updated.getSortOrder());
        assertNotNull(updated.getText());
        assertEquals(target.getId(), updated.getId());
        assertEquals(target.getSortOrder(), updated.getSortOrder());
        assertEquals(target.getText(), updated.getText());
        assertEquals(target.getPoll(), updated.getPoll());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void deleteById() {
        questionService.deleteById(question.getId());
        questionService.findById(question.getId());
    }
}