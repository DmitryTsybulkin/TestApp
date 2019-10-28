package com.test.app.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO {

    private Long id;
    private Long pollId;
    private String text;
    private Integer sortOrder;

}
