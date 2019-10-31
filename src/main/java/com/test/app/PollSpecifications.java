package com.test.app;

import com.test.app.entities.Poll;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;

public class PollSpecifications {

    public static class PollWithName implements Specification<Poll> {
        private final String name;
        public PollWithName(String name) {
            this.name = name;
        }
        @Override
        public Predicate toPredicate(Root<Poll> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
            if (name == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(false));
            }
            return criteriaBuilder.equal(root.get("name"), this.name);
        }
    }

    public static class PollWithStartDate implements Specification<Poll> {
        private final LocalDateTime startDate;
        public PollWithStartDate(LocalDateTime startDate) {
            this.startDate = startDate;
        }
        @Override
        public Predicate toPredicate(Root<Poll> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
            if (startDate == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(false));
            }
            return criteriaBuilder.lessThan(root.get("startDate"), this.startDate);
        }
    }

    public static class PollWithEndDate implements Specification<Poll> {
        private final LocalDateTime endDate;
        public PollWithEndDate(LocalDateTime endDate) {
            this.endDate = endDate;
        }
        @Override
        public Predicate toPredicate(Root<Poll> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
            if (endDate == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(false));
            }
            return criteriaBuilder.greaterThan(root.get("endDate"), this.endDate);
        }
    }

    public static class PollWithActive implements Specification<Poll> {
        private final Boolean active;
        public PollWithActive(Boolean active) {
            this.active = active;
        }
        @Override
        public Predicate toPredicate(Root<Poll> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
            if (active == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(false));
            }
            return criteriaBuilder.equal(root.get("active"), this.active);
        }
    }

}
