package fastcampus.scheduling.user.user.service;

import fastcampus.scheduling.user.dto.UserRequest;
import fastcampus.scheduling.user.dto.UserResponse;
import fastcampus.scheduling.user.user.model.User;
import fastcampus.scheduling.user.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse.JoinDTO save(UserRequest.JoinDTO joinDTO){
//        if(joinDTO == null)
//            throw new
        User user = joinDTO.toEntityWithHashPassword(passwordEncoder);
        User persistencUser = userRepository.save(user);
        return UserResponse.JoinDTO.from(persistencUser);
    }

}
