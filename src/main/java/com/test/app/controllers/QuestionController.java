package com.test.app.controllers;

import com.test.app.dtos.QuestionDTO;
import com.test.app.representatons.QuestionRepresentation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Api
@RestController
public class QuestionController {

    private final QuestionRepresentation questionRepresentation;

    @Autowired
    public QuestionController(QuestionRepresentation questionRepresentation) {
        this.questionRepresentation = questionRepresentation;
    }

    @PostMapping("/questions/create")
    @ApiOperation("Создать новый вопрос")
    public QuestionDTO createPoll(@RequestBody QuestionDTO dto) {
        return questionRepresentation.createQuestion(dto);
    }

    @GetMapping("/questions")
    @ApiOperation("Поиск всех вопросов ил по параметрам")
    public Page<QuestionDTO> findAllPolls(@RequestParam Pageable pageable) {
        return questionRepresentation.findAllQuestions(pageable);
    }

    @ApiOperation("Обновить существующий вопрос")
    @PatchMapping("/questions/{id}/update")
    public QuestionDTO updatePoll(@PathVariable Long id, @RequestBody QuestionDTO dto) {
        dto.setId(id);
        return questionRepresentation.updateQuestion(dto);
    }

    @ApiOperation("Удалить вопрос")
    @DeleteMapping("/questions/{id}/delete")
    public void deletePoll(@PathVariable Long id) {
        questionRepresentation.deleteQuestionById(id);
    }

}
