package fastcampus.scheduling.user.user.controller;

import fastcampus.scheduling._core.util.ApiResponse;
import fastcampus.scheduling.user.dto.UserRequest;
import fastcampus.scheduling.user.dto.UserResponse;
import fastcampus.scheduling.user.user.service.UserService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserApiController {

    private final UserService userService;

    @PostMapping("/api/join")
    public ResponseEntity<ApiResponse.Result<UserResponse.JoinDTO>> join(
        @RequestBody @Valid UserRequest.JoinDTO joinDTO, Errors errors) {

        log.info("/user/join POST " + joinDTO);

        UserResponse.JoinDTO joinResponse = userService.save(joinDTO);
        return ResponseEntity.ok(ApiResponse.success(joinResponse));
    }
}
