package com.fastcampus.scheduling.user.service;

import com.fastcampus.scheduling._core.common.Constants;
import com.fastcampus.scheduling._core.errors.ErrorMessage;
import com.fastcampus.scheduling._core.errors.exception.Exception400;
import com.fastcampus.scheduling._core.errors.exception.Exception401;
import com.fastcampus.scheduling._core.security.dto.UserPrincipal;
import com.fastcampus.scheduling.admin.dto.AdminRequest;
import com.fastcampus.scheduling.user.common.Position;
import com.fastcampus.scheduling.user.dto.UserRequest;
import com.fastcampus.scheduling.user.dto.UserRequest.UpdateDTO;
import com.fastcampus.scheduling.user.model.User;
import com.fastcampus.scheduling.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

		private final UserRepository userRepository;
		private final PasswordEncoder passwordEncoder;

		@Override
		public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
			User user =  userRepository.findByUserEmail(username)
					.orElseThrow(() -> new UsernameNotFoundException(ErrorMessage.MISMATCH_SIGN_IN_INFO));

			Collection<GrantedAuthority> authorities = getAuthorities(user);

			return new UserPrincipal(user, authorities);
		}

		public Collection<GrantedAuthority> getAuthorities(User user) {
			Collection<GrantedAuthority> authorities = new ArrayList<>();
			String authority = "ROLE_";
			authority += user.getPosition() == Position.MANAGER ? "MANAGER" : "USER";
			authorities.add(new SimpleGrantedAuthority(authority));
			return authorities;
		}

		@Transactional(readOnly = true)
		public User findByUserId(Long userId) {

			return userRepository.findById(userId)
					.orElseThrow(() -> new Exception401(
							ErrorMessage.USER_NOT_FOUND));
		}

		@Transactional
		public User updateUser(User user, UpdateDTO updateDTO) throws IllegalArgumentException, OptimisticLockingFailureException{

				String userPassword = updateDTO.getUserPassword();
				if (userPassword != null) {
					user.setUserPassword(passwordEncoder.encode((userPassword)));
				}

				String phoneNumber = updateDTO.getPhoneNumber();
				if (phoneNumber != null) {
					user.setPhoneNumber(phoneNumber);
				}

				String profileThumbUrl = updateDTO.getProfileThumbUrl();
				if (profileThumbUrl != null) {
					user.setProfileThumbUrl(profileThumbUrl);
				}

				return userRepository.save(user);
		}

		@Transactional
		public void resetUserPassword(AdminRequest.ResetPasswordDTO resetPasswordDTO) {
				Long userId = resetPasswordDTO.getUserId();

				User user = userRepository.findById(userId)
						.orElseThrow(() -> new UsernameNotFoundException(ErrorMessage.NOT_FOUND_USER_FOR_UPDATE));

				String newPassword = resetPasswordDTO.getResetPassword();
				user.setUserPassword(passwordEncoder.encode((newPassword)));
		}

		@Transactional
		public User save(UserRequest.SignUpDTO signUpDTO) throws IllegalArgumentException, OptimisticLockingFailureException {
				validateSignUp(signUpDTO);

				User user = signUpDTO.toEntityWithHashPassword(passwordEncoder);
				user.setUsedVacation(Constants.USED_VACATION);

				return userRepository.save(user);
		}

		@Transactional(readOnly = true)
		public void validateSignUp(UserRequest.SignUpDTO signUpDTO) {
				if(signUpDTO == null)
						throw new Exception400(ErrorMessage.EMPTY_DATA_FOR_USER_SIGNUP);

				if(signUpDTO.getPosition().equals(Position.MANAGER))
						throw new Exception400(ErrorMessage.INVALID_POSITION);

				String phoneNumber = signUpDTO.getPhoneNumber();

				User duplicatedPhoneNumber = userRepository.findByPhoneNumber(phoneNumber)
						.orElse(null);

				if(duplicatedPhoneNumber != null)
						throw new Exception400(ErrorMessage.DUPLICATE_PHONENUMBER);

				if(phoneNumber.isBlank())
						throw new Exception400(ErrorMessage.EMPTY_DATA_FOR_USER_CHECK_PHONENUMBER);
		}
}