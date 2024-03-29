package com.test.app.services;

import com.test.app.entities.Poll;
import com.test.app.excpetions.EntryDuplicateException;
import com.test.app.excpetions.ResourceNotFoundException;
import com.test.app.repositories.PollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PollService {

    private final PollRepository pollRepository;

    @Autowired
    public PollService(PollRepository pollRepository) {
        this.pollRepository = pollRepository;
    }

    @Transactional
    public Poll create(Poll poll) {
        if (pollRepository.existsByName(poll.getName())) {
            throw new EntryDuplicateException("Опрос с таким названием уже есть");
        }
        return pollRepository.save(poll);
    }

    @Transactional(readOnly = true)
    public Poll findById(Long id) {
        return pollRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Опрос по id: " + id + " не найден"));
    }

    @Transactional(readOnly = true)
    public List<Poll> findAll() {
        return pollRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Poll> findAll(Specification<Poll> specification) {
        return pollRepository.findAll(specification);
    }

    @Transactional
    public Poll update(Poll poll) {
        Poll target = findById(poll.getId());
        target.setActive(poll.getActive());
        target.setName(poll.getName());
        target.setStartDate(poll.getStartDate());
        target.setEndDate(poll.getEndDate());
        return pollRepository.save(target);
    }

    @Transactional
    public void deleteById(Long id) {
        Poll target = findById(id);
        pollRepository.delete(target);
    }

}
