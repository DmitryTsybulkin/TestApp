package com.test.app.services;

import com.test.app.entities.Poll;
import com.test.app.entities.Question;
import com.test.app.excpetions.EntryDuplicateException;
import com.test.app.excpetions.ResourceNotFoundException;
import com.test.app.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Transactional
    public Question create(Question question) {
        if (questionRepository.existsByTextAndPoll(question.getText(), question.getPoll())) {
            throw new EntryDuplicateException("Вопрос с таким текстом для опроса: " +
                    question.getPoll().getName() + " уже есть");
        } else if (questionRepository.existsBySortOrderAndPoll(question.getSortOrder(), question.getPoll())) {
            throw new EntryDuplicateException("Вопрос под номером: " + question.getSortOrder() + " уже есть в опросе");
        }
        return questionRepository.save(question);
    }

    @Transactional(readOnly = true)
    public Question findById(Long id) {
        return questionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Вопрос по id: " + id + " не найден"));
    }

    @Transactional(readOnly = true)
    public List<Question> findAll() {
        return questionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Question> findAllByPoll(Poll poll) {
        return questionRepository.findAllByPoll(poll);
    }

    @Transactional
    public Question update(Question question) {
        Question target = findById(question.getId());
        target.setSortOrder(question.getSortOrder());
        target.setPoll(question.getPoll());
        target.setText(question.getText());
        return questionRepository.save(target);
    }

    @Transactional
    public void deleteById(Long id) {
        Question target = findById(id);
        questionRepository.delete(target);
    }

}
