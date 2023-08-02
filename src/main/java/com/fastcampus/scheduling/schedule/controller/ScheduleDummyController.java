package com.fastcampus.scheduling.schedule.controller;

import com.fastcampus.scheduling._core.util.ApiResponse;
import com.fastcampus.scheduling._core.util.ApiResponse.Result;
import com.fastcampus.scheduling.schedule.common.ScheduleType;
import com.fastcampus.scheduling.schedule.common.State;
import com.fastcampus.scheduling.schedule.dto.ScheduleResponse.GetScheduleDTO;
import com.fastcampus.scheduling.schedule.model.DummySchedule;
import com.fastcampus.scheduling.user.model.DummyUser;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dummy")
public class ScheduleDummyController {

	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@GetMapping("/schedule/list")
	public ResponseEntity<Result<List<GetScheduleDTO>>> getTotalSchedule() {
		DummyUser user1 = DummyUser.builder()
				.userName("Kim")
				.userEmail("kim@gmail.com")
				.build();

		DummyUser user2 = DummyUser.builder()
				.userName("Park")
				.userEmail("park@gmail.com")
				.build();;

		DummyUser user3 = DummyUser.builder()
				.userName("Jon")
				.userEmail("jon@gmail.com")
				.build();;

		List<GetScheduleDTO> scheduleDTOList = new ArrayList<>();

		DummySchedule dummySchedule = DummySchedule.builder()
				.scheduleType(ScheduleType.ANNUAL)
				.startDate(LocalDate.parse("2023-08-01", formatter))
				.endDate(LocalDate.parse("2023-08-05", formatter))
				.state(State.PENDING)
				.build();

		scheduleDTOList.add(GetScheduleDTO.from(dummySchedule, user1));

		dummySchedule = DummySchedule.builder()
				.scheduleType(ScheduleType.DUTY)
				.startDate(LocalDate.parse("2023-08-10", formatter))
				.endDate(LocalDate.parse("2023-08-10", formatter))
				.state(State.REJECT)
				.build();

		scheduleDTOList.add(GetScheduleDTO.from(dummySchedule, user1));

		dummySchedule = DummySchedule.builder()
				.scheduleType(ScheduleType.ANNUAL)
				.startDate(LocalDate.parse("2023-08-11", formatter))
				.endDate(LocalDate.parse("2023-08-13", formatter))
				.state(State.APPROVE)
				.build();

		scheduleDTOList.add(GetScheduleDTO.from(dummySchedule, user2));

		dummySchedule = DummySchedule.builder()
				.scheduleType(ScheduleType.ANNUAL)
				.startDate(LocalDate.parse("2023-08-20", formatter))
				.endDate(LocalDate.parse("2023-08-20", formatter))
				.state(State.PENDING)
				.build();

		scheduleDTOList.add(GetScheduleDTO.from(dummySchedule, user2));

		dummySchedule = DummySchedule.builder()
				.scheduleType(ScheduleType.DUTY)
				.startDate(LocalDate.parse("2023-08-20", formatter))
				.endDate(LocalDate.parse("2023-08-20", formatter))
				.state(State.APPROVE)
				.build();

		scheduleDTOList.add(GetScheduleDTO.from(dummySchedule, user3));

		dummySchedule = DummySchedule.builder()
				.scheduleType(ScheduleType.DUTY)
				.startDate(LocalDate.parse("2023-08-24", formatter))
				.endDate(LocalDate.parse("2023-08-24", formatter))
				.state(State.REJECT)
				.build();

		scheduleDTOList.add(GetScheduleDTO.from(dummySchedule, user3));

		return ResponseEntity.ok(ApiResponse.success(scheduleDTOList));
	}

}
