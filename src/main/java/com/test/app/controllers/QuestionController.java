package com.test.app.controllers;

import com.test.app.dtos.QuestionDTO;
import com.test.app.representatons.QuestionRepresentation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public QuestionDTO createQuestion(@RequestBody QuestionDTO dto) {
        return questionRepresentation.createQuestion(dto);
    }

    @GetMapping("/questions/{id}")
    @ApiOperation("Поиск вопроса по id")
    public QuestionDTO findQuestionById(@PathVariable Long id) {
        return questionRepresentation.findQuestionById(id);
    }

    @GetMapping("/questions")
    @ApiOperation("Поиск всех вопросов или по параметрам")
    public List<QuestionDTO> findAllQuestions() {
        return questionRepresentation.findAllQuestions();
    }

    @ApiOperation("Обновить существующий вопрос")
    @PatchMapping("/questions/{id}")
    public QuestionDTO updateQuestion(@PathVariable Long id, @RequestBody QuestionDTO dto) {
        dto.setId(id);
        return questionRepresentation.updateQuestion(dto);
    }

    @ApiOperation("Удалить вопрос")
    @DeleteMapping("/questions/{id}")
    public void deleteQuestion(@PathVariable Long id) {
        questionRepresentation.deleteQuestionById(id);
    }

}
