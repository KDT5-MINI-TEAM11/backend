package fastcampus.scheduling.user.user.service;

import fastcampus.scheduling.user.user.model.User;
import fastcampus.scheduling.user.user.repository.UserRepository;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findByUserId(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new IllegalStateException("유저를 찾을 수 없습니다."));
    }

    public User updateUser(Long id, String userPassword,String phoneNumber, String profileThumbUrl) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalStateException("유저를 찾을 수 없습니다."));

        user.setUserPassword(userPassword);
        user.setPhoneNumber(phoneNumber);
        user.setProfileThumbUrl(profileThumbUrl);

        return userRepository.save(user);
    }
}
