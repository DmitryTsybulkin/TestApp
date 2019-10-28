package com.test.app.representatons;

import com.test.app.dtos.PollDTO;
import com.test.app.entities.Poll;
import com.test.app.services.PollService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ContextConfiguration(classes = PollRepresentation.class)
@RunWith(MockitoJUnitRunner.class)
public class PollRepresentationTest {

    @Mock
    private PollService pollService;
    private PollRepresentation pollRepresentation;

    private Poll poll = Poll.builder()
            .id(1L)
            .name("name")
            .startDate(LocalDateTime.now())
            .endDate(LocalDateTime.now().plusDays(1))
            .active(true)
            .build();
    private PollDTO pollDTO = PollDTO.builder()
            .id(2L)
            .name("another")
            .startDate(LocalDateTime.now().minusDays(2))
            .endDate(LocalDateTime.now().plusDays(2))
            .active(true)
            .build();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        pollRepresentation = new PollRepresentation(pollService);
    }

    @Test
    public void createPoll() {
        Mockito.when(pollService.create(any(Poll.class))).thenReturn(poll);
        PollDTO dto = pollRepresentation.createPoll(pollDTO);
        assertNotNull(dto);
        assertNotNull(dto.getId());
        assertNotNull(dto.getActive());
        assertNotNull(dto.getEndDate());
        assertNotNull(dto.getName());
        assertNotNull(dto.getStartDate());
        assertEquals(poll.getName(), dto.getName());
        assertEquals(poll.getEndDate(), dto.getEndDate());
        assertEquals(poll.getStartDate(), dto.getStartDate());
        assertEquals(poll.getActive(), dto.getActive());
    }

    @Test
    public void findPollById() {
        Mockito.when(pollService.findById(anyLong())).thenReturn(poll);
        PollDTO dto = pollRepresentation.findPollById(1L);
        assertNotNull(dto);
        assertNotNull(dto.getId());
        assertNotNull(dto.getActive());
        assertNotNull(dto.getName());
        assertNotNull(dto.getStartDate());
        assertNotNull(dto.getEndDate());
        assertEquals(poll.getId(), dto.getId());
        assertEquals(poll.getName(), dto.getName());
        assertEquals(poll.getStartDate(), dto.getStartDate());
        assertEquals(poll.getEndDate(), dto.getEndDate());
        assertEquals(poll.getActive(), dto.getActive());
    }

    @Test
    public void findAllPolls() {
        Mockito.when(pollService.findAll(Pageable.unpaged()))
                .thenReturn(new PageImpl<>(Collections.singletonList(poll)));
        Page<PollDTO> all = pollRepresentation.findAllPolls(Pageable.unpaged());
        assertNotNull(all);
        assertEquals(1, all.getNumberOfElements());
    }

    @Test
    public void updatePoll() {
        Mockito.when(pollService.update(any(Poll.class))).thenReturn(poll);
        PollDTO dto = pollRepresentation.updatePoll(pollDTO);
        assertNotNull(dto);
        assertNotNull(dto.getId());
        assertNotNull(dto.getName());
        assertNotNull(dto.getActive());
        assertNotNull(dto.getStartDate());
        assertNotNull(dto.getEndDate());
        assertEquals(poll.getId(), dto.getId());
        assertEquals(poll.getActive(), dto.getActive());
        assertEquals(poll.getName(), dto.getName());
        assertEquals(poll.getStartDate(), dto.getStartDate());
        assertEquals(poll.getEndDate(), dto.getEndDate());
    }

    @Test
    public void fromDto() {
        Poll result = pollRepresentation.fromDto(pollDTO);
        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getName());
        assertNotNull(result.getActive());
        assertNotNull(result.getStartDate());
        assertNotNull(result.getEndDate());
    }

    @Test
    public void toDto() {
        PollDTO dto = pollRepresentation.toDto(poll);
        assertNotNull(dto);
        assertNotNull(dto.getId());
        assertNotNull(dto.getName());
        assertNotNull(dto.getActive());
        assertNotNull(dto.getStartDate());
        assertNotNull(dto.getEndDate());
    }

}