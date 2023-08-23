package com.fastcampus.scheduling.email.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class EmailResponse {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AuthEmailDTO {

        private String authNumber;

        public static AuthEmailDTO from(String authNumber) {
            return AuthEmailDTO.builder()
                .authNumber(authNumber)                  //반환하는 인증값 암호화 해야될듯
                .build();
        }
    }
}
