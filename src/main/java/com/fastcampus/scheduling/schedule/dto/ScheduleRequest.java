package com.fastcampus.scheduling.schedule.dto;

import com.fastcampus.scheduling.schedule.common.ScheduleType;
import com.fastcampus.scheduling.schedule.model.Schedule;
import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.Nullable;

public class ScheduleRequest {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class ModifyScheduleDTO {

        @NotBlank
        private  Long id;

        @NotBlank
        private LocalDate startDate;

        @NotBlank
        private LocalDate endDate;

        public static ModifyScheduleDTO from(Schedule schedule) {
            return ModifyScheduleDTO.builder()
                .id(schedule.getId())
                .startDate(schedule.getStartDate().toLocalDate())
                .endDate(schedule.getEndDate().toLocalDate())
                .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Setter
    @ToString
    public static class AddScheduleDTO {
        @Nullable
        private Long userId;
        @NotBlank
        private ScheduleType scheduleType;

        @NotBlank
        private LocalDate startDate;
        @Nullable
        private LocalDate endDate;

    }
}
