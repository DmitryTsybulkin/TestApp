package com.test.app.repositories;

import com.test.app.entities.Poll;
import com.test.app.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    Boolean existsByTextAndPoll(String text, Poll poll);
    Boolean existsBySortOrderAndPoll(Integer sortOrder, Poll poll);
    List<Question> findAllByPoll(Poll poll);
}
