package com.fastcampus.scheduling.schedule.common;

import java.util.Objects;

public enum ScheduleType {

	ANNUAL,
	DUTY;

	public static ScheduleType from(String name) {
		for (ScheduleType scheduleType : ScheduleType.values()) {
			if (Objects.equals(scheduleType.name(), name)) return scheduleType;
		}

		throw new RuntimeException();
		//throw new Exception500("권한 매칭 오류");
	}
}
