package com.test.app.entities;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Table
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Poll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean active;

    public Poll(String name, LocalDateTime startDate, LocalDateTime endDate, Boolean active) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.active = active;
    }
}
