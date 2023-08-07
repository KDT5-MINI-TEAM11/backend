package com.fastcampus.scheduling.email.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;


public class EmailRequest {

    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SendEmailDTO {

        @NotBlank
        @Pattern(regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")
        private String to;
    }

    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CheckEmailDTO {

        @NotBlank
        @Pattern(regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")
        private String userEmail;
    }

    @Getter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CheckEmailAuthDTO {

        @NotBlank
        @Pattern(regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")
        private String userEmail;

        @NotBlank
        private String userEmailAuth;
    }
}
