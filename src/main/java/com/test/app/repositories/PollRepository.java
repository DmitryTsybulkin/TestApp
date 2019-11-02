package com.test.app.repositories;

import com.test.app.entities.Poll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PollRepository extends JpaRepository<Poll, Long>, JpaSpecificationExecutor<Poll> {
    Boolean existsByName(String name);
}
