package com.fastcampus.scheduling.schedule.dto;

import com.fastcampus.scheduling.schedule.common.ScheduleType;
import com.fastcampus.scheduling.schedule.common.State;
import com.fastcampus.scheduling.schedule.model.DummySchedule;
import com.fastcampus.scheduling.schedule.model.Schedule;
import com.fastcampus.scheduling.user.model.DummyUser;
import com.fastcampus.scheduling.user.model.User;
import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

public class ScheduleResponse {

	@Getter
	@Builder
	public static class GetUserScheduleDTO {

		private ScheduleType scheduleType;

		private State state;

		private LocalDate startDate;

		private LocalDate endDate;

		public static GetUserScheduleDTO from(Schedule schedule) {
			return GetUserScheduleDTO.builder()
				.scheduleType(schedule.getScheduleType())
				.state(schedule.getState())
				.startDate(schedule.getStartDate())
				.endDate(schedule.getEndDate())
				.build();
		}
	}

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@Setter
	@ToString
	public static class AddScheduleDTO {

		@NotBlank
		private Long userId;

		@NotBlank
		private ScheduleType scheduleType;

		@NotBlank
		private LocalDate startDate;

		@NotBlank
		private LocalDate endDate;

		@NotBlank
		private State state;

		public static ScheduleResponse.AddScheduleDTO from(Schedule schedule) {
			return AddScheduleDTO.builder()
				.userId(schedule.getUser().getId())
				.scheduleType(schedule.getScheduleType())
				.startDate(schedule.getStartDate())
				.endDate(schedule.getEndDate())
				.state(schedule.getState())
				.build();
		}
	}

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@ToString
	public static class GetAllScheduleDTO {

		private String userName;

		private String userEmail;

		private ScheduleType scheduleType;

		private LocalDate startDate;

		private LocalDate endDate;

		private State state;

		public static GetAllScheduleDTO from(Schedule schedule) {
			User user = schedule.getUser();

			return GetAllScheduleDTO.builder()
				.userName(user.getUserName())
				.userEmail(user.getUserEmail())
				.scheduleType(schedule.getScheduleType())
				.startDate(schedule.getStartDate())
				.endDate(schedule.getEndDate())
				.state(schedule.getState())
				.build();
		}
	}

	@Getter
	@Builder
	public static class GetScheduleDTO {

		private String userName;

		private String userEmail;

		private ScheduleType scheduleType;

		private LocalDate startDate;

		private LocalDate endDate;

		private State state;

		public static GetScheduleDTO from(DummySchedule schedule, DummyUser user) {
			return GetScheduleDTO.builder()
					.userName(user.getUserName())
					.userEmail(user.getUserEmail())
					.scheduleType(schedule.getScheduleType())
					.startDate(schedule.getStartDate())
					.endDate(schedule.getEndDate())
					.state(schedule.getState())
					.build();
		}
	}
}
