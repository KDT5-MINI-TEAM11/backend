package com.fastcampus.scheduling.admin.dto;

import com.fastcampus.scheduling.user.common.Position;
import javax.validation.constraints.NotBlank;
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
    public static class ApproveDTO {
        @NotBlank
        private Long id;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class RejectDTO {
        @NotBlank
        private Long id;
    }
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class PendingDTO {
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
}
