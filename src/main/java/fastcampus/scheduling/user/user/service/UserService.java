package fastcampus.scheduling.user.user.service;

import fastcampus.scheduling.user.user.model.User;
import fastcampus.scheduling.user.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    //private PasswordEncoder passwordEncoder;


    /*
    // 유저 ID로 유저 정보 조회
    public User findByUserId(Long userId) {
        return userRepository.findByUserId(userId);
    }

    // 유저 Email로 유저 정보 조회
    public User findUserByUserEmail(String userEmail) {
        return userRepository.findByUserEmail(userEmail);
    }

     유저 정보 업데이트
    public void update(User user, UserRequest.UpdateDTO updateDTO) {

        //Long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        //User user = userRepository.findById(userId).orElse(null);

        user.setUserEmail(updateDTO.getUserEmail());
        //user.getUserPassword(updateDTO.getUserPassword());
        //user.setUserPassword(passwordEncoder.encode("test1234"));
        user.setPhoneNumber(updateDTO.getPhoneNumber());
        user.setProfileThumbUrl(updateDTO.getProfileThumbUrl());

    }

    public User updateUser(User updatedUser) {
        User currentUser = findByUserId(updatedUser.getId());
        // 변경 가능한 필드만 업데이트합니다.
        currentUser.setProfileThumbUrl(updatedUser.getProfileThumbUrl());
        currentUser.setPhoneNumber(updatedUser.getPhoneNumber());
        currentUser.setUserPassword(updatedUser.getUserPassword());
        return userRepository.save(currentUser);
    }

     */

    public User findByUserId(Long id) {
        return userRepository.findByUserId(id);
    }

    public User getUserByEmail(String userEmail) {
        return userRepository.findByUserEmail(userEmail);
    }

    public User updateUser(User updatedUser) {
        return userRepository.save(updatedUser);
    }

}
