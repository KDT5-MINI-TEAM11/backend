package fastcampus.scheduling.user.service;

import fastcampus.scheduling.user.dto.UserRequest;
import fastcampus.scheduling.user.dto.UserResponse;
import fastcampus.scheduling.user.repository.UserRepository;
import fastcampus.scheduling.user.model.User;
import lombok.AllArgsConstructor;

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
//    @Transactional
//    public UserResponse.SignUpDTO save(UserRequest.SignUpDTO signUpDTO){
//        if(signUpDTO == null) throw new Exception500(ErrorMessage.EMPTY_DATA_FOR_USER_SIGNUP);
//
//        User user = signUpDTO.toEntityWithHashPassword(passwordEncoder);
//        User persistencUser = userRepository.save(user);
//
//        //access token을 만들어서 반환해줘야 할듯
//
//        Long id = persistencUser.getId(); //임시
//        String userName = persistencUser.getUserName();
//        String userInfo = id + userName;
//
//        String accessToken = id + userName;
//        return UserResponse.SignUpDTO.from(accessToken);
//    }
//
//    @Transactional(readOnly = true)
//    public UserResponse.SignInDTO findByUserEmail(UserRequest.SignUpDTO signUpDTO){
//        Optional<User> userOptional = userRepository.findByUserEmail(signUpDTO.getUserEmail());
//
//        return UserResponse.SignInDTO.builder()
//            //todo
//            .build();
//    }
//
//    @Transactional(readOnly = true)
//    public void checkPhone(UserRequest.CheckPhoneDTO checkPhoneDTO) {
//        validatePhone(checkPhoneDTO);
//        Optional<User> userOptional = userRepository.findByPhoneNumber(checkPhoneDTO.getPhoneNumber());
//    }
//
//    private static void validatePhone(UserRequest.CheckPhoneDTO checkPhoneDTO) {
//        //String regex = "/^[A-Za-z0-9_\\.\\-]+@[A-Za-z0-9\\-]+\\.[A-Za-z0-9\\-]+/";
//        String phoneNumber = checkPhoneDTO.getPhoneNumber();
//
//        if(checkPhoneDTO == null || checkPhoneDTO.getPhoneNumber().isBlank())
//            throw new Exception400(phoneNumber, ErrorMessage.EMPTY_DATA_FOR_USER_CHECK_PhoneNumber);
////        if (!phoneNumber.matches(regex))
////            throw new Exception400(phoneNumber, ErrorMessage.INVALID_PhoneNumber);
//
//    }
}

