package com.fastcampus.scheduling.schedule.dto;

import com.fastcampus.scheduling.schedule.common.ScheduleType;
import com.fastcampus.scheduling.schedule.common.State;
import com.fastcampus.scheduling.schedule.model.Schedule;
import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

public class ScheduleRequest {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class ModifyScheduleDTO {

        @NotBlank
        private LocalDate startDate;

        @NotBlank
        private LocalDate endDate;

        public static ModifyScheduleDTO from(Schedule schedule) {
            return ModifyScheduleDTO.builder()
                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .build();
        }
    }
}
