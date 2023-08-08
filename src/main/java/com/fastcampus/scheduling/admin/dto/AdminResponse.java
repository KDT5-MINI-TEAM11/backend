package com.fastcampus.scheduling.admin.dto;

import com.fastcampus.scheduling.schedule.common.ScheduleType;
import com.fastcampus.scheduling.schedule.common.State;
import com.fastcampus.scheduling.schedule.model.Schedule;
import com.fastcampus.scheduling.user.common.Position;
import com.fastcampus.scheduling.user.model.User;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AdminResponse {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetAllScheduleDTO{
        private Long id;
        private String userName;
        private Position position;
        private ScheduleType type;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private State state;

        public static GetAllScheduleDTO from(Schedule schedule) {
            return GetAllScheduleDTO.builder()
                .id(schedule.getId())
                .userName(schedule.getUser().getUserName())
                .position(schedule.getUser().getPosition())
                .type(schedule.getScheduleType())
                .startDate(LocalDateTime.of(schedule.getStartDate().toLocalDate(), LocalTime.MIN))
                .endDate(LocalDateTime.of(schedule.getEndDate().toLocalDate(), LocalTime.MAX))
                .state(schedule.getState())
                .build();
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetAllUserDTO{
        private Long id;
        private String userName;
        private String profileThumbUrl;
        private Position position;
        private LocalDateTime createAt;

        public static GetAllUserDTO from(User user) {
            return GetAllUserDTO.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .profileThumbUrl(user.getProfileThumbUrl())
                .position(user.getPosition())
                .createAt(LocalDateTime.of(user.getCreatedAt().toLocalDate(), LocalTime.MIN))
                .build();
        }
    }
}
