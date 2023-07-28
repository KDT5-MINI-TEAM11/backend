package fastcampus.scheduling._core.security;

import fastcampus.scheduling._core.errors.ErrorMessage;
import fastcampus.scheduling._core.errors.exception.Exception500;
import fastcampus.scheduling.user.user.model.User;
import fastcampus.scheduling.user.user.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PrincipalUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        log.info("스프링 시큐리티 로그인 서비스 호출 userEmail: " + userEmail);

        Optional<User> userOptional = userRepository.findByUserEmail(userEmail);
        User user = userOptional.orElseThrow(() -> new Exception500(ErrorMessage.LOGIN_FAILED));

        return new PrincipalUserDetail(user);
    }
}
