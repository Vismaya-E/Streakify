package com.litmus7.streakify.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Entity
@Table(name="habit_logs" ,uniqueConstraints = {
        @UniqueConstraint(columnNames={"habit_id","log_date"})
})

@Data
@NoArgsConstructor
public class HabitLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="habit_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Habit habit;

    @Column(name = "log_date" ,nullable = false)
    private LocalDate logDate;

    @Column(nullable = false)
    private boolean completed;

}
