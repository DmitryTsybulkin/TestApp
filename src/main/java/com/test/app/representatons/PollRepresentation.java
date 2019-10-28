package com.test.app.representatons;

import com.test.app.dtos.PollDTO;
import com.test.app.entities.Poll;
import com.test.app.services.PollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

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

    public Page<PollDTO> findAllPolls(Pageable pageable) {
        return pollService.findAll(pageable).map(this::toDto);
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