package com.test.app.controllers;

import com.test.app.PollSpecifications;
import com.test.app.dtos.PollDTO;
import com.test.app.dtos.QuestionDTO;
import com.test.app.entities.Poll;
import com.test.app.representatons.PollRepresentation;
import com.test.app.representatons.QuestionRepresentation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

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
    @ApiOperation("Поиск всех опросов или по параметрам")
    public Page<PollDTO> findAllPolls(@RequestParam Pageable pageable,
                                      @RequestParam(required = false) String name,
                                      @RequestParam(required = false) Boolean active,
                                      @RequestParam(name = "from", required = false) LocalDateTime startDate,
                                      @RequestParam(name = "to", required = false) LocalDateTime endDate) {
        Specification<Poll> specification = Specification.where(new PollSpecifications.PollWithName(name))
                .and(new PollSpecifications.PollWithActive(active))
                .and(new PollSpecifications.PollWithStartDate(startDate))
                .and(new PollSpecifications.PollWithEndDate(endDate));
        return pollRepresentation.findAllPolls(pageable, specification);
    }

    @GetMapping(value = "/polls/{id}/questions", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Поиск всех опросов ил по параметрам")
    public Page<QuestionDTO> findQuestionsByPoll(@PathVariable Long id,
                                                 @RequestParam Pageable pageable) {
        return questionRepresentation.findAllQuestionsByPollId(pageable, id);
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
