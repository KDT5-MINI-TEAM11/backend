package fastcampus.scheduling.user.controller;

import fastcampus.scheduling._core.util.ApiResponse;
import fastcampus.scheduling.user.dto.UserResponse;
import fastcampus.scheduling.user.dto.UserResponse.GetUserHeaderDTO;
import fastcampus.scheduling.user.model.User;
import fastcampus.scheduling.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping("/api/v1/user/userHeader")
    public ResponseEntity<ApiResponse.Result<UserResponse.GetUserHeaderDTO>> getUserHead() {
        log.info("/api/v1/user/userHeader GET ");

        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        User user = userService.getUserById(userId);

        int totalVacation = user.getPosition().getTotalVacation();
        int usedVacation = user.getUsedVacation();

        GetUserHeaderDTO getUserHeaderDTO = GetUserHeaderDTO.from(user, usedVacation, totalVacation);

        return ResponseEntity.ok(ApiResponse.success(getUserHeaderDTO));
    }
}
