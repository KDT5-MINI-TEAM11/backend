package com.fastcampus.scheduling.admin.dto;

import com.fastcampus.scheduling.schedule.common.State;
import com.fastcampus.scheduling.user.common.Position;
import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class AdminRequest {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class ApproveDTO {
        @NotBlank
        private Long userId;
        @NotNull
        private State state;
        @NotBlank
        private LocalDate startDate;
        @NotBlank
        private LocalDate endDate;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class RejectDTO {
        @NotBlank
        private Long userId;
        @NotNull
        private State state;
        @NotBlank
        private LocalDate startDate;
        @NotBlank
        private LocalDate endDate;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class UpdatePositionDTO{
        @NotBlank
        private Long id;
        @NotBlank
        private Position position;
    }
}
