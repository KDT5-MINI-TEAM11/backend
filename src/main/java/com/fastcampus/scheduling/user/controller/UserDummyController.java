package com.fastcampus.scheduling.user.controller;

import com.fastcampus.scheduling._core.util.ApiResponse;
import com.fastcampus.scheduling._core.util.ApiResponse.Result;
import com.fastcampus.scheduling.schedule.common.ScheduleType;
import com.fastcampus.scheduling.schedule.common.State;
import com.fastcampus.scheduling.schedule.model.DummySchedule;
import com.fastcampus.scheduling.user.dto.UserRequest;
import com.fastcampus.scheduling.user.dto.UserRequest.AddDummyScheduleDTO;
import com.fastcampus.scheduling.user.dto.UserRequest.CancelDummyScheduleDTO;
import com.fastcampus.scheduling.user.dto.UserResponse;
import com.fastcampus.scheduling.user.dto.UserResponse.GetDummyScheduleDTO;
import com.fastcampus.scheduling.user.dto.UserResponse.ModifyDummyScheduleDTO;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dummy")
public class UserDummyController {

	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@GetMapping("/user/schedule")
	public ResponseEntity<Result<List<GetDummyScheduleDTO>>> getUserSchedule() {

		List<GetDummyScheduleDTO> scheduleDTOList = new ArrayList<>();

		DummySchedule dummySchedule = DummySchedule.builder()
				.scheduleType(ScheduleType.ANNUAL)
				.startDate(LocalDateTime.parse("2023-08-01", formatter))
				.endDate(LocalDateTime.parse("2023-08-05", formatter))
				.state(State.REJECT)
				.build();

		scheduleDTOList.add(GetDummyScheduleDTO.from(dummySchedule));

		dummySchedule = DummySchedule.builder()
				.scheduleType(ScheduleType.ANNUAL)
				.startDate(LocalDateTime.parse("2023-08-05", formatter))
				.endDate(LocalDateTime.parse("2023-08-05", formatter))
				.state(State.PENDING)
				.build();

		scheduleDTOList.add(GetDummyScheduleDTO.from(dummySchedule));

		dummySchedule = DummySchedule.builder()
				.scheduleType(ScheduleType.ANNUAL)
				.startDate(LocalDateTime.parse("2023-08-01", formatter))
				.endDate(LocalDateTime.parse("2023-08-01", formatter))
				.state(State.APPROVE)
				.build();

		scheduleDTOList.add(GetDummyScheduleDTO.from(dummySchedule));

		return ResponseEntity.ok(ApiResponse.success(scheduleDTOList));
	}

	@PostMapping("/user/schedule/add")
	public ResponseEntity<Result<UserResponse.AddDummyScheduleDTO>> addUserSchedule(@RequestBody AddDummyScheduleDTO addDummyScheduleDTO) {
		DummySchedule dummySchedule = DummySchedule.builder()
				.scheduleType(addDummyScheduleDTO.getScheduleType())
				.startDate(LocalDateTime.parse(addDummyScheduleDTO.getStartDate().toString(), formatter))
				.endDate(LocalDateTime.parse(addDummyScheduleDTO.getEndDate().toString(), formatter))
				.state(State.PENDING)
				.build();

		return ResponseEntity.ok(ApiResponse.success(UserResponse.AddDummyScheduleDTO.from(dummySchedule)));
	}

	@PostMapping("/user/schedule/cancel")
	public ResponseEntity<Result<UserResponse.CancelDummyScheduleDTO>> cancelUserSchedule(@RequestBody CancelDummyScheduleDTO cancelDummyScheduleDTO) {

		return ResponseEntity.ok(ApiResponse.success(UserResponse.CancelDummyScheduleDTO.from("정상적으로 취소 되었습니다.")));
	}

	@PatchMapping("/user/schedule/modify")
	public ResponseEntity<Result<ModifyDummyScheduleDTO>> updateUserSchedule(@RequestBody UserRequest.ModifyScheduleDTO modifyScheduleDTO) {
		DummySchedule dummySchedule = DummySchedule.builder()
				.scheduleType(ScheduleType.ANNUAL)
				.startDate(LocalDateTime.parse(modifyScheduleDTO.getStartDate().toString(), formatter))
				.endDate(LocalDateTime.parse(modifyScheduleDTO.getEndDate().toString(), formatter))
				.state(State.PENDING)
				.build();

		return ResponseEntity.ok(ApiResponse.success(ModifyDummyScheduleDTO.from(dummySchedule)));
	}

}
