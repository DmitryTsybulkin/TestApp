package com.test.app.controllers;

import com.test.app.specifications.PollSpecifications;
import com.test.app.dtos.PollDTO;
import com.test.app.dtos.QuestionDTO;
import com.test.app.entities.Poll;
import com.test.app.representatons.PollRepresentation;
import com.test.app.representatons.QuestionRepresentation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Api
@RestController
public class PollController {

    private final PollRepresentation pollRepresentation;
    private final QuestionRepresentation questionRepresentation;

    @Autowired
    public PollController(PollRepresentation pollRepresentation, QuestionRepresentation questionRepresentation) {
        this.pollRepresentation = pollRepresentation;
        this.questionRepresentation = questionRepresentation;
    }

    @PostMapping(value = "/polls/create", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Создать новый опрос")
    public PollDTO createPoll(@RequestBody PollDTO dto) {
        return pollRepresentation.createPoll(dto);
    }

    @GetMapping(value = "/polls/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Опрос по id")
    public PollDTO findPollById(@PathVariable Long id) {
        return pollRepresentation.findPollById(id);
    }

    @GetMapping(value = "/polls", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Поиск всех опросов или по параметрам", produces = "application/json")
    public List<PollDTO> findAllPolls(@ApiParam @RequestParam(required = false) String name,
                                      @ApiParam @RequestParam(required = false) String active,
                                      @ApiParam(format = "yyyy-MM-dd'T'HH:mm:ss.SSS")
                                          @RequestParam(required = false) String startDate,
                                      @ApiParam(format = "yyyy-MM-dd'T'HH:mm:ss.SSS")
                                          @RequestParam(required = false) String endDate) {
        Specification<Poll> specification = Specification.where(new PollSpecifications.PollWithName(name))
                .and(new PollSpecifications.PollWithActive(active != null ? Boolean.valueOf(active) : null))
                .and(new PollSpecifications.PollWithStartDate(startDate != null ? LocalDateTime.parse(startDate) : null))
                .and(new PollSpecifications.PollWithEndDate(endDate != null ? LocalDateTime.parse(endDate) : null));
        return pollRepresentation.findAllPolls(specification);
    }

    @GetMapping(value = "/polls/{id}/questions", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Поиск всех опросов ил по параметрам")
    public List<QuestionDTO> findQuestionsByPoll(@PathVariable Long id) {
        return questionRepresentation.findAllQuestionsByPollId(id);
    }

    @PatchMapping(value = "/polls/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PollDTO updatePoll(@PathVariable Long id, @RequestBody PollDTO dto) {
        dto.setId(id);
        return pollRepresentation.updatePoll(dto);
    }

    @DeleteMapping("/polls/{id}")
    public void deletePoll(@PathVariable Long id) {
        pollRepresentation.deletePollById(id);
    }

}
