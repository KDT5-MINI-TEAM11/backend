package com.fastcampus.scheduling.schedule.dto;

import com.fastcampus.scheduling.schedule.common.ScheduleType;
import com.fastcampus.scheduling.schedule.common.State;
import com.fastcampus.scheduling.schedule.model.DummySchedule;
import com.fastcampus.scheduling.schedule.model.Schedule;
import com.fastcampus.scheduling.user.model.DummyUser;
import com.fastcampus.scheduling.user.model.User;
import java.time.LocalDateTime;
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

		private  Long id;

		private ScheduleType scheduleType;

		private State state;

		private LocalDateTime startDate;

		private LocalDateTime endDate;

		public static GetUserScheduleDTO from(Schedule schedule) {
			return GetUserScheduleDTO.builder()
				.id(schedule.getId())
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
		private LocalDateTime startDate;

		@NotBlank
		private LocalDateTime endDate;

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

		private Long id;

		private String userName;

		private ScheduleType scheduleType;

		private LocalDateTime startDate;

		private LocalDateTime endDate;

		private State state;

		public static GetAllScheduleDTO from(Schedule schedule) {
			User user = schedule.getUser();

			return GetAllScheduleDTO.builder()
				.id(schedule.getId())
				.userName(user.getUserName())
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

		private LocalDateTime startDate;

		private LocalDateTime endDate;

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

	@Getter
	@Builder
	public static class ModifyScheduleDTO {
		private ScheduleType type;
		private LocalDateTime startDate;
		private LocalDateTime endDate;
		private State state;
		public static ModifyScheduleDTO from(Schedule schedule) {
			return ModifyScheduleDTO.builder()
				.type(schedule.getScheduleType())
				.startDate(schedule.getStartDate())
				.endDate(schedule.getEndDate())
				.state(schedule.getState())
				.build();
		}
	}
}
