package fastcampus.scheduling.user.service;

import static fastcampus.scheduling._core.errors.ErrorMessage.EMPTY_DATA_FOR_USER_SIGNUP;
import static fastcampus.scheduling._core.errors.ErrorMessage.MISMATCH_SIGN_IN_INFO;
import static fastcampus.scheduling._core.errors.ErrorMessage.NOT_FOUND_USER_FOR_UPDATE;
import static fastcampus.scheduling._core.errors.ErrorMessage.USER_NOT_FOUND;

import fastcampus.scheduling._core.errors.ErrorMessage;
import fastcampus.scheduling._core.errors.exception.DuplicatePhoneNumberException;
import fastcampus.scheduling._core.errors.exception.Exception400;
import fastcampus.scheduling._core.errors.exception.Exception401;
import fastcampus.scheduling._core.errors.exception.Exception500;
import fastcampus.scheduling._core.util.JwtTokenProvider;
import fastcampus.scheduling.user.dto.UserRequest;
import fastcampus.scheduling.user.model.User;
import fastcampus.scheduling.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
    private final JwtTokenProvider jwtTokenProvider;

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
    public User save(UserRequest.SignUpDTO signUpDTO) throws IllegalArgumentException, OptimisticLockingFailureException {
        if(signUpDTO == null) throw new Exception400(EMPTY_DATA_FOR_USER_SIGNUP);

        User user = signUpDTO.toEntityWithHashPassword(passwordEncoder);
				user.setUsedVacation(0);
        return userRepository.save(user);
    }


    @Transactional(readOnly = true)
    public void checkPhone(UserRequest.CheckPhoneDTO checkPhoneDTO) {
        if(checkPhoneDTO == null) throw new Exception500(ErrorMessage.EMPTY_DATA_FOR_USER_CHECK_PhoneNumber);
        validatePhone(checkPhoneDTO);
    }

    @Transactional(readOnly = true)
    public void validatePhone(UserRequest.CheckPhoneDTO checkPhoneDTO) {
        String phoneNumber = checkPhoneDTO.getPhoneNumber();
        Optional<User> userOptional = userRepository.findByPhoneNumber(phoneNumber);

        if(phoneNumber.isBlank())
            throw new Exception400(ErrorMessage.EMPTY_DATA_FOR_USER_CHECK_PhoneNumber);
        if(userOptional.isPresent())
            throw new DuplicatePhoneNumberException();
    }

    public Authentication getAuthentication(String email) {
        UserDetails userDetails = loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
            userDetails.getAuthorities());
    }
}