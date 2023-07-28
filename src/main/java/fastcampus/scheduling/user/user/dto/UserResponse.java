package fastcampus.scheduling.user.user.dto;

import java.util.Collection;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserResponse {

    @Getter
    @Builder
    public static class SignUpDTO {
        private String accessToken;

        public static SignUpDTO from(String accessToken) { //todo token객체로
            return SignUpDTO.builder()
                .accessToken(accessToken)
                .build();
        }
    }

    @Getter
    @Builder
    public static class SignInDTO implements UserDetails {
        private String accessToken;

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return null;
        }

        @Override
        public String getPassword() {
            return null;
        }

        @Override
        public String getUsername() {
            return null;
        }

        @Override
        public boolean isAccountNonExpired() {
            return false;
        }

        @Override
        public boolean isAccountNonLocked() {
            return false;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return false;
        }

        @Override
        public boolean isEnabled() {
            return false;
        }
    }


    @Getter
    @Builder
    public static class AuthEmail{
        private String authNumber;

        public static AuthEmail from(String authNumber) { //email에 따로 빼서 만들기
            return AuthEmail.builder()
                .authNumber(authNumber)                  //반환하는 인증값 암호화 해야될듯
                .build();
        }
    }
}
