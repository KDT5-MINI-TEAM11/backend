package com.fastcampus.scheduling.user.dto;

import com.fastcampus.scheduling.schedule.common.ScheduleType;
import com.fastcampus.scheduling.schedule.model.DummySchedule;
import com.fastcampus.scheduling.user.common.Position;
import com.fastcampus.scheduling.user.model.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserRequest {

    @ToString
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SignUpDTO {
        @NotBlank
        @Pattern(regexp = "^[a-zA-Z0-9.%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,}$")
        private String userEmail;

        @NotBlank
        @Size(min = 8, max = 16)
        @Pattern(regexp = "^(?=.*\\d)(?=.*[!@#$%^&*()-+=])(?=.*[a-zA-Z]).{8,16}$")
        private String userPassword;

        @NotBlank
        @Size(min = 2, max = 20)
        private String userName;

        @NotNull
        private Position position;

        @NotBlank
        private String phoneNumber;

        private String profileThumbUrl;

        public User toEntityWithHashPassword(PasswordEncoder passwordEncoder) {
            String encodedPassword = passwordEncoder.encode(this.userPassword);
            return User.builder()
                .userEmail(userEmail)
                .userPassword(encodedPassword)
                .userName(userName)
                .profileThumbUrl(profileThumbUrl)
                .position(position)
                .phoneNumber(phoneNumber)
                .build();
        }
    }

    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignInDTO {

        @NotBlank
        @Pattern(regexp = "^[a-zA-Z0-9.%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,}$")
        private String userEmail;

        @NotBlank
        @Pattern(regexp = "^(?=.*\\d)(?=.*[!@#$%^&*()-+=])(?=.*[a-zA-Z]).{8,16}$")
        private String userPassword;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class UpdateDTO {

        @NotBlank
        private String userPassword;

        private String profileThumbUrl;

        @NotBlank
        private String phoneNumber;

        public static UpdateDTO from(User user) {
            return UpdateDTO.builder()
                .userPassword(user.getUserPassword())
                .profileThumbUrl(user.getProfileThumbUrl())
                .phoneNumber(user.getPhoneNumber())
                .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class AddDummyScheduleDTO {

        @NotBlank
        private ScheduleType scheduleType;

        @NotBlank
        private LocalDateTime startDate;

        @NotBlank
        private LocalDateTime endDate;

        public static AddDummyScheduleDTO from(DummySchedule schedule) {
            return AddDummyScheduleDTO.builder()
                .scheduleType(schedule.getScheduleType())
                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .build();
        }
    }

    public static class CancelDummyScheduleDTO {


    }

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

        public static ModifyScheduleDTO from(DummySchedule schedule) {
            return ModifyScheduleDTO.builder()
                .startDate(schedule.getStartDate().toLocalDate())
                .endDate(schedule.getEndDate().toLocalDate())
                .build();
        }
    }
}
