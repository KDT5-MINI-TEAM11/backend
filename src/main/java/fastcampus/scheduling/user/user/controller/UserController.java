package fastcampus.scheduling.user.user.controller;

import com.sun.xml.bind.v2.TODO;
import fastcampus.scheduling.user.dto.UserRequest;
import fastcampus.scheduling.user.user.model.User;
import fastcampus.scheduling.user.user.repository.UserRepository;
import fastcampus.scheduling.user.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

/*
    // 마이페이지 조회
    //@AuthenticationPrincipal PrincipalUser userData 추후 추가
    @GetMapping("/api/v1/user/updateInfo")
    public ResponseEntity<User> myPageInfo(Model model, User user) {
        //Authentication authentication = new UsernamePasswordAuthenticationToken(userId), null, authenities);
        //SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        user.setUserName(user.getUserName());

        //model.addAttribute("user", userData.getUserName());
        //model.addAttribute("email", userData.getUserEmail());
        //model.addAttribute("position", userData.getPosition());
        //model.addAttribute("phoneNumber", userData.getPhoneNumber());
        //model.addAttribute("profileThumbUrl", userData.getProfileThumbUrl());
        //model.addAttribute("remainingVacation", userData.getRemainingVacation());

        model.addAttribute("user", user.getUserName());
        model.addAttribute("email", user.getUserEmail());
        model.addAttribute("position", user.getPosition());
        model.addAttribute("phoneNumber", user.getPhoneNumber());
        model.addAttribute("profileThumbUrl", user.getProfileThumbUrl());
        model.addAttribute("remainingVacation", user.getRemainingVacation());

        User currentUser = userService.findByUserId(user.getId());
        return new ResponseEntity<>(currentUser, HttpStatus.OK);

    }

     마이페이지 정보 수정
     @AuthenticationPrincipal PrincipalUserDetail userData - 추후 추가 예정
    @PutMapping("/api/v1/user/info")
    public  ResponseEntity<User>  updateMyPageInfo(@RequestBody User update, Long id, UserRequest.UpdateDTO updateDTO) {
        //Authentication authentication = new UsernamePasswordAuthenticationToken(userId), null, authenities);
        //Long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        // getPrincipal 추가 예정
        //User user = userRepository.findByUserId(id);

        //userService.update(user, updateDTO);

        User updatedUserInfo = userService.update(id);
        return new ResponseEntity<>(updatedUserInfo, HttpStatus.OK);
    }

    @PutMapping("/api/v1/user/updateInfo")
    public ResponseEntity<User> updateMyPageInfo(@RequestBody User updatedUser) {
        User updatedUserInfo = userService.updateUser(updatedUser);
        return new ResponseEntity<>(updatedUserInfo, HttpStatus.OK);
    }

 */

    @GetMapping("/api/v1/user/Info")
    public ResponseEntity<User> getMyPage() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByEmail(userEmail);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/api/v1/user/Info")
    public ResponseEntity<User> updateMyProfile(@RequestBody User updatedUser) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userService.getUserByEmail(userEmail);

        if (currentUser != null) {
            currentUser.setProfileThumbUrl(updatedUser.getProfileThumbUrl());
            currentUser.setPhoneNumber(updatedUser.getPhoneNumber());
            currentUser.setUserPassword(updatedUser.getUserPassword());

            User updatedUsers = userService.updateUser(currentUser);
            if (updatedUsers != null) {
                return ResponseEntity.ok(updatedUsers);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
