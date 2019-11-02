package com.test.app.representatons;

import com.test.app.dtos.QuestionDTO;
import com.test.app.entities.Poll;
import com.test.app.entities.Question;
import com.test.app.services.PollService;
import com.test.app.services.QuestionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ContextConfiguration(classes = QuestionRepresentation.class)
@RunWith(MockitoJUnitRunner.class)
public class QuestionRepresentationTest {

    @Mock
    private PollService pollService;
    @Mock
    private QuestionService questionService;

    private QuestionRepresentation questionRepresentation;

    private Poll poll = Poll.builder()
            .id(1L)
            .name("name")
            .startDate(LocalDateTime.now())
            .endDate(LocalDateTime.now().plusDays(1))
            .active(true)
            .build();
    private Question question = Question.builder()
            .id(1L)
            .sortOrder(1)
            .text("anyText")
            .poll(poll)
            .build();
    private QuestionDTO questionDTO = QuestionDTO.builder()
            .id(question.getId())
            .text(question.getText())
            .sortOrder(question.getSortOrder())
            .pollId(poll.getId())
            .build();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        questionRepresentation = new QuestionRepresentation(questionService, pollService);
    }

    @Test
    public void createQuestion() {
        Mockito.when(pollService.findById(anyLong())).thenReturn(poll);
        Mockito.when(questionService.create(any(Question.class))).thenReturn(question);
        QuestionDTO dto = questionRepresentation.createQuestion(questionDTO);
        assertNotNull(dto);
        assertNotNull(dto.getId());
        assertNotNull(dto.getPollId());
        assertNotNull(dto.getSortOrder());
        assertNotNull(dto.getText());
        assertEquals(questionDTO.getPollId(), dto.getPollId());
        assertEquals(questionDTO.getText(), dto.getText());
        assertEquals(questionDTO.getSortOrder(), dto.getSortOrder());
    }

    @Test
    public void findQuestionById() {
        Mockito.when(questionService.findById(anyLong())).thenReturn(question);
        QuestionDTO dto = questionRepresentation.findQuestionById(question.getId());
        assertNotNull(dto);
        assertNotNull(dto.getId());
        assertNotNull(dto.getPollId());
        assertNotNull(dto.getSortOrder());
        assertNotNull(dto.getText());
        assertEquals(question.getPoll().getId(), dto.getPollId());
        assertEquals(question.getText(), dto.getText());
        assertEquals(question.getSortOrder(), dto.getSortOrder());
    }

    @Test
    public void findAllQuestionsByPollId() {
        Mockito.when(questionService.findAllByPoll(any(Poll.class))).thenReturn(Collections.singletonList(question));
        Mockito.when(pollService.findById(anyLong())).thenReturn(poll);
        List<QuestionDTO> all = questionRepresentation.findAllQuestionsByPollId(poll.getId());
        assertNotNull(all);
        assertEquals(1, all.size());
    }

    @Test
    public void findAllQuestions() {
        Mockito.when(questionService.findAll()).thenReturn(Collections.singletonList(question));
        List<QuestionDTO> all = questionRepresentation.findAllQuestions();
        assertNotNull(all);
        assertEquals(1, all.size());
    }

    @Test
    public void updateQuestion() {
        Mockito.when(questionService.update(any(Question.class))).thenReturn(question);
        QuestionDTO dto = questionRepresentation.updateQuestion(questionDTO);
        assertNotNull(dto);
        assertNotNull(dto.getId());
        assertNotNull(dto.getText());
        assertNotNull(dto.getSortOrder());
        assertEquals(question.getPoll().getId(), dto.getPollId());
        assertEquals(question.getText(), dto.getText());
        assertEquals(question.getSortOrder(), dto.getSortOrder());
    }

    @Test
    public void fromDto() {
        Mockito.when(pollService.findById(anyLong())).thenReturn(poll);
        Question result = questionRepresentation.fromDto(questionDTO);
        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getPoll());
        assertNotNull(result.getText());
        assertNotNull(result.getSortOrder());
    }

    @Test
    public void toDto() {
        QuestionDTO dto = questionRepresentation.toDto(question);
        assertNotNull(dto);
        assertNotNull(dto.getId());
        assertNotNull(dto.getPollId());
        assertNotNull(dto.getText());
        assertNotNull(dto.getSortOrder());
    }
}