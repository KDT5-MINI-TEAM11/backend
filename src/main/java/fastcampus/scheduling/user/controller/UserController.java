package fastcampus.scheduling.user.controller;

import fastcampus.scheduling._core.util.ApiResponse;
import fastcampus.scheduling.user.dto.UserRequest;
import fastcampus.scheduling.user.dto.UserResponse;
import fastcampus.scheduling.user.dto.UserResponse.GetMyPageDTO;
import fastcampus.scheduling.user.dto.UserResponse.GetUserHeaderDTO;
import fastcampus.scheduling.user.model.User;
import fastcampus.scheduling.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping("/api/v1/user/userHeader")
    public ResponseEntity<ApiResponse.Result<GetUserHeaderDTO>> getUserHead() {
        log.info("/api/v1/user/userHeader GET ");

        Long userId = Long.valueOf(
						SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        User user = userService.findByUserId(userId);

        int totalVacation = user.getPosition().getTotalVacation();
        int usedVacation = user.getUsedVacation();

        GetUserHeaderDTO getUserHeaderDTO = GetUserHeaderDTO.from(user, usedVacation, totalVacation);

        return ResponseEntity.ok(ApiResponse.success(getUserHeaderDTO));
    }
    @GetMapping("/api/v1/user/info")
    public ResponseEntity<ApiResponse.Result<UserResponse.GetMyPageDTO>> getMyPage() {

        Long userId = Long.valueOf(
            SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());

        User user = userService.findByUserId(userId);
        GetMyPageDTO getMyPageDTO = GetMyPageDTO.from(user);

        return ResponseEntity.ok(ApiResponse.success(getMyPageDTO));

    }

    @PutMapping("/api/v1/user/info")
    public ResponseEntity<ApiResponse.Result<UserResponse.GetMyPageDTO>> updateMyPage(@RequestBody UserRequest.UpdateDTO updateDTO) {

        Long userId = Long.valueOf(
            SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());

        String userPassword = updateDTO.getUserPassword();
        String phoneNumber = updateDTO.getPhoneNumber();
        String profileThumbUrl = updateDTO.getProfileThumbUrl();

        User user = userService.findByUserId(userId);

        user.setUserPassword(userPassword);
        user.setPhoneNumber(phoneNumber);
        user.setProfileThumbUrl(profileThumbUrl);

        User updatedUser = userService.updateUser(userId, userPassword, phoneNumber, profileThumbUrl);

        GetMyPageDTO getMyPageDTO = GetMyPageDTO.from(updatedUser);

        return ResponseEntity.ok(ApiResponse.success(getMyPageDTO));

    }


}
