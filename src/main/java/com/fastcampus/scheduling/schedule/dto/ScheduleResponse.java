package com.fastcampus.scheduling.schedule.dto;

import com.fastcampus.scheduling.schedule.common.ScheduleType;
import com.fastcampus.scheduling.schedule.common.State;
import com.fastcampus.scheduling.schedule.model.DummySchedule;
import com.fastcampus.scheduling.user.model.DummyUser;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

public class ScheduleResponse {
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
