package fastcampus.scheduling.user.dto;

import fastcampus.scheduling.user.common.Position;
import fastcampus.scheduling.user.user.model.User;
import lombok.Builder;
import lombok.Getter;

public class UserResponse {

    @Getter
    @Builder
    public static class JoinDTO {

        private String userEmail;


        public static JoinDTO from(User user) {
            return JoinDTO.builder()
                .userEmail(user.getUserEmail())
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

}
