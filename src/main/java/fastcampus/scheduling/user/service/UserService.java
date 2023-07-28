package fastcampus.scheduling.user.service;

import fastcampus.scheduling._core.security.exception.AuthExceptionMessage;
import fastcampus.scheduling.user.exception.UserExceptionMessage;
import fastcampus.scheduling.user.exception.UserNotExistException;
import fastcampus.scheduling.user.model.User;
import fastcampus.scheduling.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user =  userRepository.findByUserEmail(username)
        .orElseThrow(() -> new UsernameNotFoundException(AuthExceptionMessage.MISMATCH_SIGN_IN_INFO.getMessage()));
    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(user.getPosition().name()));
    //set name as userId(table pk)
    return new org.springframework.security.core.userdetails.User(user.getId().toString(), user.getUserPassword(), authorities);
  }

  public User getUserById(Long userId) {
    return userRepository.findById(userId).orElseThrow(() -> new UserNotExistException(
        UserExceptionMessage.USER_NOT_FOUND_EXCEPTION.getMessage()));
  }

	public User findByUserId(Long id) {
			return userRepository.findById(id)
					.orElseThrow(() -> new IllegalStateException("유저를 찾을 수 없습니다."));
	}

	public User updateUser(Long id, String userPassword, String phoneNumber,
			String profileThumbUrl) {
			User user = userRepository.findById(id)
					.orElseThrow(() -> new IllegalStateException("유저를 찾을 수 없습니다."));

			user.setUserPassword(userPassword);
			user.setPhoneNumber(phoneNumber);
			user.setProfileThumbUrl(profileThumbUrl);

			return userRepository.save(user);
	}

}
