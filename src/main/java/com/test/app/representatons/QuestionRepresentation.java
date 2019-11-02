package com.test.app.representatons;

import com.test.app.dtos.QuestionDTO;
import com.test.app.entities.Question;
import com.test.app.services.PollService;
import com.test.app.services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class QuestionRepresentation {

    private final QuestionService questionService;
    private final PollService pollService;

    @Autowired
    public QuestionRepresentation(QuestionService questionService, PollService pollService) {
        this.questionService = questionService;
        this.pollService = pollService;
    }

    public QuestionDTO createQuestion(QuestionDTO dto) {
        return toDto(questionService.create(fromDto(dto)));
    }

    public QuestionDTO findQuestionById(Long id) {
        return toDto(questionService.findById(id));
    }

    public List<QuestionDTO> findAllQuestionsByPollId(Long pollId) {
        return questionService.findAllByPoll(pollService.findById(pollId)).stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<QuestionDTO> findAllQuestions() {
        return questionService.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public QuestionDTO updateQuestion(QuestionDTO dto) {
        return toDto(questionService.update(fromDto(dto)));
    }

    public void deleteQuestionById(Long id) {
        questionService.deleteById(id);
    }

    public Question fromDto(QuestionDTO dto) {
        return Question.builder()
                .id(dto.getId())
                .text(dto.getText())
                .sortOrder(dto.getSortOrder())
                .poll(pollService.findById(dto.getPollId()))
                .build();
    }

    public QuestionDTO toDto(Question question) {
        return QuestionDTO.builder()
                .id(question.getId())
                .pollId(question.getPoll().getId())
                .sortOrder(question.getSortOrder())
                .text(question.getText())
                .build();
    }

}
