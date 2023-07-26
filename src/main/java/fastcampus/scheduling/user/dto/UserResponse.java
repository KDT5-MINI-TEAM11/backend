package fastcampus.scheduling.user.dto;

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

}
