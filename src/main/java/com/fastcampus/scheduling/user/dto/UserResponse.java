package com.fastcampus.scheduling.user.dto;

import com.fastcampus.scheduling.schedule.common.ScheduleType;
import com.fastcampus.scheduling.schedule.common.State;
import com.fastcampus.scheduling.schedule.model.DummySchedule;
import com.fastcampus.scheduling.user.common.Position;
import com.fastcampus.scheduling.user.model.User;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Getter;

public class UserResponse {

    @Getter
    @Builder
    public static class SignUpDTO {
        private String accessToken;

        public static SignUpDTO from(String accessToken) {
            return SignUpDTO.builder()
                .accessToken(accessToken)
                .build();
        }
    }


    @Getter
    @Builder
    public static class GetMyPageDTO {

        private String userName;
        private String userEmail;
        private String profileThumbUrl;
        private Position position;
        private String phoneNumber;
        private int usedVacation;

        public static GetMyPageDTO from(User user) {
            return GetMyPageDTO.builder()
                .userName(user.getUserName())
                .userEmail(user.getUserEmail())
                .profileThumbUrl(user.getProfileThumbUrl())
                .position(user.getPosition())
                .phoneNumber(user.getPhoneNumber())
                .usedVacation(user.getUsedVacation())
                .build();
        }
    }

    @Getter
    @Builder
    public static class GetUserHeaderDTO {

        private String userName;
        private Position position;
        private String profileThumbNail;
        private int usedVacation;

        public static GetUserHeaderDTO from(User user) {
            return GetUserHeaderDTO.builder()
                .userName(user.getUserName())
                .position(user.getPosition())
                .profileThumbNail(user.getProfileThumbUrl())
                .usedVacation(user.getUsedVacation())
                .build();
        }

    }

    @Getter
    @Builder
    public static class GetDummyScheduleDTO {

        private ScheduleType scheduleType;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private State state;

        public static GetDummyScheduleDTO from(DummySchedule schedule) {
            return GetDummyScheduleDTO.builder()
                .scheduleType(schedule.getScheduleType())
                .startDate(LocalDateTime.of(schedule.getStartDate().toLocalDate(), LocalTime.MIN))
                .endDate(LocalDateTime.of(schedule.getEndDate().toLocalDate(), LocalTime.MAX))
                .state(schedule.getState())
                .build();
        }
    }

    @Getter
    @Builder
    public static class AddDummyScheduleDTO {

        private ScheduleType scheduleType;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private State state;

        public static AddDummyScheduleDTO from(DummySchedule schedule) {
            return AddDummyScheduleDTO.builder()
                .scheduleType(schedule.getScheduleType())
                .startDate(LocalDateTime.of(schedule.getStartDate().toLocalDate(), LocalTime.MIN))
                .endDate(LocalDateTime.of(schedule.getEndDate().toLocalDate(), LocalTime.MAX))
                .state(schedule.getState())
                .build();
        }
    }

    @Getter
    @Builder
    public static class CancelDummyScheduleDTO {

        private String message;

        public static CancelDummyScheduleDTO from(String message) {
            return CancelDummyScheduleDTO.builder()
                .message(message)
                .build();
        }
    }

    @Getter
    @Builder
    public static class ModifyDummyScheduleDTO {

        private ScheduleType scheduleType;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private State state;

        public static ModifyDummyScheduleDTO from(DummySchedule schedule) {
            return ModifyDummyScheduleDTO.builder()
                .scheduleType(schedule.getScheduleType())
                .startDate(LocalDateTime.of(schedule.getStartDate().toLocalDate(), LocalTime.MIN))
                .endDate(LocalDateTime.of(schedule.getEndDate().toLocalDate(), LocalTime.MAX))
                .state(schedule.getState())
                .build();
        }
    }
}
