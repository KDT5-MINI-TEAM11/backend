package com.fastcampus.scheduling.schedule.dto;

import com.fastcampus.scheduling.schedule.common.ScheduleType;
import com.fastcampus.scheduling.schedule.common.State;
import com.fastcampus.scheduling.schedule.model.Schedule;
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

		private  Long id;

		private ScheduleType scheduleType;

		private State state;

		private LocalDate startDate;

		private LocalDate endDate;

		public static GetUserScheduleDTO from(Schedule schedule) {
			return GetUserScheduleDTO.builder()
				.id(schedule.getId())
				.scheduleType(schedule.getScheduleType())
				.state(schedule.getState())
				.startDate(schedule.getStartDate().toLocalDate())
				.endDate(schedule.getEndDate().toLocalDate())
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
		private Long id;

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
				.id(schedule.getId())
				.scheduleType(schedule.getScheduleType())
				.startDate(schedule.getStartDate().toLocalDate())
				.endDate(schedule.getEndDate().toLocalDate())
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

		private String userEmail;

		private ScheduleType scheduleType;

		private LocalDate startDate;

		private LocalDate endDate;

		private State state;

		public static GetAllScheduleDTO from(Schedule schedule) {
			User user = schedule.getUser();

			return GetAllScheduleDTO.builder()
				.id(schedule.getId())
				.userName(user.getUserName())
				.userEmail(user.getUserEmail())
				.scheduleType(schedule.getScheduleType())
				.startDate(schedule.getStartDate().toLocalDate())
				.endDate(schedule.getEndDate().toLocalDate())
				.state(schedule.getState())
				.build();
		}
	}

	@Getter
	@Builder
	public static class ModifyScheduleDTO {
		private ScheduleType type;
		private LocalDate startDate;
		private LocalDate endDate;
		private State state;
		public static ModifyScheduleDTO from(Schedule schedule) {
			return ModifyScheduleDTO.builder()
				.type(schedule.getScheduleType())
				.startDate(schedule.getStartDate().toLocalDate())
				.endDate(schedule.getEndDate().toLocalDate())
				.state(schedule.getState())
				.build();
		}
	}
}
