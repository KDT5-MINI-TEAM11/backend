package fastcampus.scheduling.user.user.controller;

import fastcampus.scheduling._core.util.ApiResponse;
import fastcampus.scheduling.user.dto.UserRequest;
import fastcampus.scheduling.user.dto.UserResponse;
import fastcampus.scheduling.user.dto.UserResponse.GetMyPageDTO;
import fastcampus.scheduling.user.user.model.User;
import fastcampus.scheduling.user.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @GetMapping("/api/v1/user/info")
    public ResponseEntity<ApiResponse.Result<UserResponse.GetMyPageDTO>> getMyPage() {
        Long userId = 1L;

        User user = userService.findByUserId(userId);
        GetMyPageDTO getMyPageDTO = GetMyPageDTO.from(user);

        return ResponseEntity.ok(ApiResponse.success(getMyPageDTO));

    }

    @PutMapping("/api/v1/user/info")
    public ResponseEntity<ApiResponse.Result<UserResponse.GetMyPageDTO>> updateMyPage(@RequestBody UserRequest.UpdateDTO updateDTO) {
        Long userId = 1L;

        String phoneNumber = updateDTO.getPhoneNumber();
        String profileThumbUrl = updateDTO.getProfileThumbUrl();

        User user = userService.findByUserId(userId);

        user.setPhoneNumber(phoneNumber);
        user.setProfileThumbUrl(profileThumbUrl);

        User updatedUser = userService.updateUser(userId, phoneNumber, profileThumbUrl);

        GetMyPageDTO getMyPageDTO = GetMyPageDTO.from(updatedUser);

        return ResponseEntity.ok(ApiResponse.success(getMyPageDTO));

    }

}
