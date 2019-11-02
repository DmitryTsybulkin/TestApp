package com.test.app.representatons;

import com.test.app.dtos.PollDTO;
import com.test.app.entities.Poll;
import com.test.app.services.PollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PollRepresentation {

    private final PollService pollService;

    @Autowired
    public PollRepresentation(PollService pollService) {
        this.pollService = pollService;
    }

    public PollDTO createPoll(PollDTO dto) {
        return toDto(pollService.create(fromDto(dto)));
    }

    public PollDTO findPollById(Long id) {
        return toDto(pollService.findById(id));
    }

    public List<PollDTO> findAllPolls() {
        return pollService.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<PollDTO> findAllPolls(Specification<Poll> specification) {
        return pollService.findAll(specification).stream().map(this::toDto).collect(Collectors.toList());
    }

    public PollDTO updatePoll(PollDTO dto) {
        return toDto(pollService.update(fromDto(dto)));
    }

    public void deletePollById(Long id) {
        pollService.deleteById(id);
    }

    public Poll fromDto(PollDTO dto) {
        return Poll.builder()
                .id(dto.getId())
                .active(dto.getActive())
                .name(dto.getName())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .build();
    }

    public PollDTO toDto(Poll poll) {
        return PollDTO.builder()
                .active(poll.getActive())
                .id(poll.getId())
                .name(poll.getName())
                .startDate(poll.getStartDate())
                .endDate(poll.getEndDate())
                .build();
    }
}