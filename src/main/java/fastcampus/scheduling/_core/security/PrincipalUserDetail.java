package fastcampus.scheduling._core.security;

import fastcampus.scheduling.user.user.model.User;
import java.util.ArrayList;
import java.util.Collection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@RequiredArgsConstructor
public class PrincipalUserDetail implements UserDetails {

    private final User user;

    @Override
    public String getPassword() {
        return user.getUserPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserEmail();
    }

    /**
     *
     * @return 계정 만료 상태 (true: 만료되지 않음)
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     *
     * @return 계정 잠금 상태 (true: 잠기지 않음)
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     *
     * @return 비밀번호 만료 상태 (true: 만료되지 않음)
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     *
     * @return 계정 활성화(사용 가능) 상태 (true: 활성화)
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     *
     * @return 계정의 권한 목록
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collectors = new ArrayList<>();
        collectors.add(() -> "POSITION_" + user.getPosition());

        return collectors;
    }
}