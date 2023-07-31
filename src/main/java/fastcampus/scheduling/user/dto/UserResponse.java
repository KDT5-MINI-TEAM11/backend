package fastcampus.scheduling.user.dto;

import fastcampus.scheduling.user.common.Position;

import fastcampus.scheduling.user.model.User;
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

        public static GetMyPageDTO from(User user) {
            return GetMyPageDTO.builder()
                .userName(user.getUserName())
                .userEmail(user.getUserEmail())
                .profileThumbUrl(user.getProfileThumbUrl())
                .position(user.getPosition())
                .phoneNumber(user.getPhoneNumber())
                .build();
        }
    }

    @Getter
    @Builder
    public static class GetUserHeaderDTO {
        private String userName;
        private String userEmail;
        private Position position;
        private String profileThumbNail;
        private int usedVacation;
        private int totalVacation;

        public static GetUserHeaderDTO from(User user, int usedVacation, int totalVacation) {
            return GetUserHeaderDTO.builder()
                .userName(user.getUserName())
                .userEmail(user.getUserEmail())
                .position(user.getPosition())
                .profileThumbNail(user.getProfileThumbUrl())
                .usedVacation(usedVacation)
                .totalVacation(totalVacation)
                .build();
        }

    }

}
