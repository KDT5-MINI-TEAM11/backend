package com.fastcampus.scheduling.schedule.model;

import com.fastcampus.scheduling.schedule.common.ScheduleType;
import com.fastcampus.scheduling.schedule.common.State;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class DummySchedule {

	@Column(nullable = false, length = 20)
	@Enumerated(EnumType.STRING)
	private ScheduleType scheduleType;

	@Column(nullable = false, length = 20)
	@Enumerated(EnumType.STRING)
	private State state;

	@Column(nullable = false)
	private LocalDateTime startDate;

	@Column(nullable = false)
	private LocalDateTime endDate;

}
