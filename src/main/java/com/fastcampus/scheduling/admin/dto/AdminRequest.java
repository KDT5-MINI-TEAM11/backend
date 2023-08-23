package com.fastcampus.scheduling.admin.dto;

import com.fastcampus.scheduling.user.common.Position;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AdminRequest {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class ScheduleDTO {
        @NotBlank
        private Long id;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class UpdatePositionDTO{
        @NotBlank
        private Long id;
        @NotBlank
        private Position position;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class ResetPasswordDTO{
        @NotBlank
        private Long userId;
        @NotBlank
        private String resetPassword;
    }
}
