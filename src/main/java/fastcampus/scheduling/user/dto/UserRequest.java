package fastcampus.scheduling.user.dto;

import fastcampus.scheduling.user.common.Position;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

public class UserRequest {

    public static class JoinDTO{
        @NotBlank
        @Pattern(regexp = "/^[A-Za-z0-9_\\.\\-]+@[A-Za-z0-9\\-]+\\.[A-Za-z0-9\\-]+/")
        private String userEmail;

        @NotBlank
        @Size(min = 8, max = 16)
        @Pattern(regexp = "/^(?=.*\\d)(?=.*[!@#$%^&*()-+=])(?=.*[a-zA-Z]).{8,16}$/")
        private String userPassword;

        @NotBlank
        @Size(min = 2, max = 20)
        private String userName;

        @NotBlank
        private Position position;

        @NotBlank
        private String phoneNumber;

        private String profileThumbUrl;


//        public User toEntityWithHashPassword(PasswordEncoder passwordEncoder) {
//            String encodedPassword = passwordEncoder.encode(this.userPassword);
//            return User.builder()
//                .userEmail(userEmail)
//                .userPassword(encodedPassword)
//                .userName(userName)
//                .position(position)
//                .phoneNumber(phoneNumber)
//                .profileThumbUrl(profileThumbUrl)
//                .build();
//        }
    }

    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginDTO {

        @NotBlank
        private String username;

        @NotBlank
        private String password;
    }
}
