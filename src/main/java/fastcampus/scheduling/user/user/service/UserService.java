package fastcampus.scheduling.user.user.service;

import fastcampus.scheduling._core.errors.ErrorMessage;
import fastcampus.scheduling._core.errors.exception.Exception400;
import fastcampus.scheduling._core.errors.exception.Exception500;
import fastcampus.scheduling.user.user.dto.UserRequest;
import fastcampus.scheduling.user.user.dto.UserResponse;
import fastcampus.scheduling.user.user.model.User;
import fastcampus.scheduling.user.user.repository.UserRepository;
import java.util.Optional;
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
    public UserResponse.SignInDTO findByUserEmail(UserRequest.SignUpDTO signUpDTO){
        Optional<User> userOptional = userRepository.findByUserEmail(signUpDTO.getUserEmail());

        return UserResponse.SignInDTO.builder()
            //todo
            .build();
    }

    @Transactional(readOnly = true)
    public void checkPhone(UserRequest.CheckPhoneDTO checkPhoneDTO) {
        validatePhone(checkPhoneDTO);
        Optional<User> userOptional = userRepository.findByPhoneNumber(checkPhoneDTO.getPhoneNumber());
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
