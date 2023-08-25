package com.fastcampus.scheduling.user.service;

import com.fastcampus.scheduling._core.errors.ErrorMessage;
import com.fastcampus.scheduling._core.security.dto.UserPrincipal;
import com.fastcampus.scheduling.user.common.Position;
import com.fastcampus.scheduling.user.model.User;
import com.fastcampus.scheduling.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomDetailService implements UserDetailsService {
	private final UserRepository userRepository;
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
}
