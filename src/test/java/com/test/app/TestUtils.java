package com.test.app;

import com.test.app.entities.Poll;
import com.test.app.entities.Question;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatterBuilder;

public class TestUtils {

    public static String parseDate(LocalDateTime time) {
        DateTimeFormatterBuilder dateTimeFormatter = new DateTimeFormatterBuilder();
        if (time.toString().endsWith("0")) {
            dateTimeFormatter.appendPattern("yyyy-MM-dd'T'HH:mm:ss.SS");
        } else {
            dateTimeFormatter.appendPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        }
        return time.format(dateTimeFormatter.toFormatter());
    }

    public static Poll stubPoll1() {
        return Poll.builder()
                .active(true)
                .name("Poll")
                .startDate(LocalDateTime.now().minusDays(2))
                .endDate(LocalDateTime.now().plusDays(2)).build();
    }

    public static Poll stubPoll2() {
        return Poll.builder()
                .active(true)
                .name("Poll_test")
                .startDate(LocalDateTime.now().minusDays(1))
                .endDate(LocalDateTime.now().plusDays(1)).build();
    }

    public static Poll stubPoll3() {
        return Poll.builder()
                .active(false)
                .name("Polly")
                .startDate(LocalDateTime.now().minusDays(3))
                .endDate(LocalDateTime.now().minusDays(1)).build();
    }

    public static Question stubQuestion1(Poll poll) {
        return Question.builder().poll(poll).sortOrder(1).text("txt").build();
    }

    public static Question stubQuestion2(Poll poll) {
        return Question.builder().poll(poll).sortOrder(2).text("test").build();
    }

}
