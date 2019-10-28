package com.test.app.services;

import com.test.app.entities.Poll;
import com.test.app.excpetions.EntryDuplicateException;
import com.test.app.excpetions.ResourceNotFoundException;
import com.test.app.repositories.PollRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PollServiceTest {

    @Autowired
    private PollService pollService;
    @Autowired
    private PollRepository pollRepository;
    private Poll poll;

    @Before
    public void setUp() {
        pollRepository.deleteAll();
        poll = pollRepository.save(Poll.builder()
                .active(true)
                .name("Корпоративы")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(5))
                .build());
    }

    @After
    public void tearDown() {
        pollRepository.deleteAll();
    }

    @Test
    public void create() {
        Poll result = pollService.create(Poll.builder()
                .active(true)
                .name("Новый опрос")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(10))
                .build());
        assertNotNull(result);
        assertNotNull(result.getId());
    }

    @Test(expected = EntryDuplicateException.class)
    public void createFailedEntryDuplicateException() {
        pollService.create(Poll.builder()
                .active(true)
                .name(poll.getName())
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(10))
                .build());
    }

    @Test
    public void findById() {
        Poll result = pollService.findById(poll.getId());
        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getActive());
        assertNotNull(result.getEndDate());
        assertNotNull(result.getName());
        assertNotNull(result.getStartDate());
        assertEquals(poll.getId(), result.getId());
        assertEquals(poll.getName(), result.getName());
        assertEquals(poll.getActive(), result.getActive());
        assertEquals(poll.getStartDate(), result.getStartDate());
        assertEquals(poll.getEndDate(), result.getEndDate());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void findByIdNotFound() {
        pollService.findById(1000L);
    }

    @Test
    public void findAll() {
        Page<Poll> all = pollService.findAll(Pageable.unpaged());
        assertNotNull(all);
        assertEquals(1, all.getNumberOfElements());
    }

    @Test
    public void update() {
        Poll origin = Poll.builder()
                .id(poll.getId())
                .startDate(LocalDateTime.now().minusDays(1))
                .endDate(LocalDateTime.now().plusDays(15))
                .name("New")
                .active(true)
                .build();
        Poll result = pollService.update(origin);
        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getActive());
        assertNotNull(result.getEndDate());
        assertNotNull(result.getName());
        assertNotNull(result.getStartDate());
        assertEquals(origin.getId(), result.getId());
        assertEquals(origin.getName(), result.getName());
        assertEquals(origin.getActive(), result.getActive());
        assertEquals(origin.getStartDate(), result.getStartDate());
        assertEquals(origin.getEndDate(), result.getEndDate());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void deleteById() {
        pollService.deleteById(poll.getId());
        pollService.findById(poll.getId());
    }
}