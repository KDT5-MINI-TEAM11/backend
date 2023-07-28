package fastcampus.scheduling.user.dto;

import fastcampus.scheduling.user.common.Position;
import fastcampus.scheduling.user.model.User;
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
