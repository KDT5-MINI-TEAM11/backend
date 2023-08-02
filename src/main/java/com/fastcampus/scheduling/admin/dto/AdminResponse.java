package com.fastcampus.scheduling.admin.dto;

import com.fastcampus.scheduling.schedule.common.ScheduleType;
import com.fastcampus.scheduling.schedule.common.State;
import com.fastcampus.scheduling.user.common.Position;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AdminResponse {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResolveDTO{
        private ScheduleType scheduleType;
        private State state;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetAllScheduleDTO{
        private Long id;
        private String userName;
        private Position position;
        private ScheduleType type;
        private LocalDate startDate;
        private LocalDate endDate;
        private State state;
    }
}
