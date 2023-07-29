package fastcampus.scheduling.user.service;

import static fastcampus.scheduling._core.errors.ErrorMessage.MISMATCH_SIGN_IN_INFO;
import static fastcampus.scheduling._core.errors.ErrorMessage.NOT_FOUND_USER_FOR_UPDATE;
import static fastcampus.scheduling._core.errors.ErrorMessage.USER_NOT_FOUND;

import fastcampus.scheduling._core.errors.ErrorMessage;
import fastcampus.scheduling._core.errors.exception.Exception400;
import fastcampus.scheduling._core.errors.exception.Exception401;
import fastcampus.scheduling._core.errors.exception.Exception500;
import fastcampus.scheduling.user.dto.UserRequest;
import fastcampus.scheduling.user.dto.UserResponse;
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
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user =  userRepository.findByUserEmail(username)
        .orElseThrow(() -> new UsernameNotFoundException(MISMATCH_SIGN_IN_INFO));
    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(user.getPosition().name()));
    //set name as userId(table pk)
    return new org.springframework.security.core.userdetails.User(user.getId().toString(), user.getUserPassword(), authorities);
  }

	public User findByUserId(Long userId) {
			return userRepository.findById(userId)
					.orElseThrow(() -> new Exception401(
							USER_NOT_FOUND));
	}

	public User updateUser(Long id, String userPassword, String phoneNumber,
			String profileThumbUrl) {
			User user = userRepository.findById(id)
					.orElseThrow(() -> new UsernameNotFoundException(NOT_FOUND_USER_FOR_UPDATE));

			user.setUserPassword(passwordEncoder.encode(userPassword));
			user.setPhoneNumber(phoneNumber);
			user.setProfileThumbUrl(profileThumbUrl);

        return userRepository.save(user);
    }
    @Transactional
    public UserResponse.SignUpDTO save(UserRequest.SignUpDTO signUpDTO){
        if(signUpDTO == null) throw new Exception500(ErrorMessage.EMPTY_DATA_FOR_USER_SIGNUP);

        User user = signUpDTO.toEntityWithHashPassword(passwordEncoder);
        User persistencUser = userRepository.save(user);

        //access token을 만들어서 반환해줘야 할듯

        Long id = persistencUser.getId(); //임시
        String userName = persistencUser.getUserName();
        String userInfo = id + userName;

        String accessToken = id + userName;
        return UserResponse.SignUpDTO.from(accessToken);
    }


    @Transactional(readOnly = true)
    public void checkPhone(UserRequest.CheckPhoneDTO checkPhoneDTO) {
        validatePhone(checkPhoneDTO);
        userRepository.findByPhoneNumber(checkPhoneDTO.getPhoneNumber());
    }

    private static void validatePhone(UserRequest.CheckPhoneDTO checkPhoneDTO) {
        //String regex = "/^[A-Za-z0-9_\\.\\-]+@[A-Za-z0-9\\-]+\\.[A-Za-z0-9\\-]+/";
        String phoneNumber = checkPhoneDTO.getPhoneNumber();

        if(checkPhoneDTO == null || checkPhoneDTO.getPhoneNumber().isBlank())
            throw new Exception400(phoneNumber, ErrorMessage.EMPTY_DATA_FOR_USER_CHECK_PhoneNumber);
//        if (!phoneNumber.matches(regex))
//            throw new Exception400(phoneNumber, ErrorMessage.INVALID_PhoneNumber);

    }
}