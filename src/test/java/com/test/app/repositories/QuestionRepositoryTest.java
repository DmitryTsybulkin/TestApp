package com.test.app.repositories;

import com.test.app.entities.Poll;
import com.test.app.entities.Question;
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

import static org.junit.Assert.*;

@Transactional
@SpringBootTest
@RunWith(SpringRunner.class)
public class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private PollRepository pollRepository;

    private Poll poll;

    private final String text = "Пользуетесь ли вы интрнетом?";
    private final int sortOrder = 1;

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
        questionRepository.save(Question.builder()
                .text(text)
                .sortOrder(sortOrder)
                .poll(poll)
                .build());
    }

    @After
    public void tearDown() {
        questionRepository.deleteAll();
        pollRepository.deleteAll();
    }

    @Test
    public void existsByText() {
        Boolean result = questionRepository.existsByTextAndPoll(text, poll);
        assertNotNull(result);
        assertTrue(result);

        Boolean result2 = questionRepository.existsByTextAndPoll("another text", poll);
        assertNotNull(result2);
        assertFalse(result2);
    }

    @Test
    public void existsBySortOrderAndPoll() {
        Boolean result = questionRepository.existsBySortOrderAndPoll(sortOrder, poll);
        assertNotNull(result);
        assertTrue(result);

        Boolean result2 = questionRepository.existsBySortOrderAndPoll(123, poll);
        assertNotNull(result2);
        assertFalse(result2);
    }

    @Test
    public void findAllByPoll() {
        Page<Question> allByPoll = questionRepository.findAllByPoll(Pageable.unpaged(), poll);
        assertNotNull(allByPoll);
        assertEquals(1, allByPoll.getNumberOfElements());
    }
}