//package com.litmus7.streakify.dto;
//
//import com.fasterxml.jackson.annotation.JsonPropertyOrder;
//import lombok.Data;
//import java.util.List;
//
//@Data
//@JsonPropertyOrder({
//        "totalHabits",
//        "activeHabits",
//        "completedToday",
//        "currentStreaks",
//        "consistencyScore"
//})
//public class DashboardResponseDTO {
//    private int totalHabits;
//    private int activeHabits;
//    private int completedToday;
//    private List<HabitStreakDTO> currentStreaks;
//    private int consistencyScore;
//}

package com.litmus7.streakify.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponseDTO {
    private int totalHabits;
    private int activeHabits;
    private int completedToday;
    private List<HabitStreakDTO> currentStreaks;
    private int consistencyScore;

}