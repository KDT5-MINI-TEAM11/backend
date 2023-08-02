package com.fastcampus.scheduling.user.service;

import com.fastcampus.scheduling._core.errors.ErrorMessage;
import com.fastcampus.scheduling._core.errors.exception.DuplicatePhoneNumberException;
import com.fastcampus.scheduling._core.errors.exception.Exception400;
import com.fastcampus.scheduling._core.errors.exception.Exception401;
import com.fastcampus.scheduling.user.common.Position;
import com.fastcampus.scheduling.user.model.User;
import com.fastcampus.scheduling.user.repository.UserRepository;
import com.fastcampus.scheduling.user.dto.UserRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
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
        .orElseThrow(() -> new UsernameNotFoundException(ErrorMessage.MISMATCH_SIGN_IN_INFO));
    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(user.getPosition().name()));
    //set name as userId(table pk)
    return new org.springframework.security.core.userdetails.User(user.getId().toString(), user.getUserPassword(), authorities);
  }

    @Transactional(readOnly = true)
	public User findByUserId(Long userId) {
			return userRepository.findById(userId)
					.orElseThrow(() -> new Exception401(
							ErrorMessage.USER_NOT_FOUND));
	}

    @Transactional
	public User updateUser(Long id, String userPassword, String phoneNumber,
			String profileThumbUrl) {
			User user = userRepository.findById(id)
					.orElseThrow(() -> new UsernameNotFoundException(ErrorMessage.NOT_FOUND_USER_FOR_UPDATE));

			user.setUserPassword(passwordEncoder.encode(userPassword));
			user.setPhoneNumber(phoneNumber);
			user.setProfileThumbUrl(profileThumbUrl);

        return userRepository.save(user);
    }

    @Transactional
    public User save(UserRequest.SignUpDTO signUpDTO) throws IllegalArgumentException, OptimisticLockingFailureException {
        if(signUpDTO == null) throw new Exception400(ErrorMessage.EMPTY_DATA_FOR_USER_SIGNUP);

        validateSignUp(signUpDTO);

        User user = signUpDTO.toEntityWithHashPassword(passwordEncoder);
		user.setUsedVacation(0);

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public void validateSignUp(UserRequest.SignUpDTO signUpDTO) {
        String phoneNumber = signUpDTO.getPhoneNumber();
        Position position = signUpDTO.getPosition();
        String userName = signUpDTO.getUserName();
        String userPassword = signUpDTO.getUserPassword();

        Optional<User> userOptional = userRepository.findByPhoneNumber(phoneNumber);

        userOptional.ifPresent(user -> {throw new DuplicatePhoneNumberException();});
        System.out.println(userName);
        System.out.println(userPassword);
        if(phoneNumber.isBlank())
            throw new Exception400(ErrorMessage.EMPTY_DATA_FOR_USER_CHECK_PHONENUMBER);
        if(position == null || position.equals(""))
            throw new Exception400(ErrorMessage.EMPTY_DATA_FOR_USER_CHECK_POSITION);
//        if(userName.isBlank() || (userName.length() >= Constants.USERNAME_MIN_SIZE && userName.length() <= Constants.USERNAME_MAX_SIZE)) //조건 수정 필요
//            throw new Exception400(ErrorMessage.INVALID_USRENAME);
//        if(userPassword.isBlank() || (userPassword.length() >= Constants.PASSWORD_MIN_SIZE && userName.length() <= Constants.PASSWORD_MAX_SIZE))
//            throw new Exception400(ErrorMessage.INVALID_PASSWORD);
    }

//    public Authentication getAuthentication(String email) {
//        UserDetails userDetails = loadUserByUsername(email);
//        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
//            userDetails.getAuthorities());
//    }
}