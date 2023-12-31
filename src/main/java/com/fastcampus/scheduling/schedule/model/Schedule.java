package com.fastcampus.scheduling.schedule.model;

import com.fastcampus.scheduling.schedule.common.ScheduleType;
import com.fastcampus.scheduling.schedule.common.State;
import com.fastcampus.scheduling.user.model.User;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "schedule_tb")
@Entity
public class Schedule {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;

	@Column(nullable = false, length = 20, name = "schedule_type")
	@Enumerated(EnumType.STRING)
	private ScheduleType scheduleType;

	@Column(nullable = false, length = 20)
	@Enumerated(EnumType.STRING)
	private State state;

	@Column(nullable = false, name ="start_date")
	private LocalDateTime startDate;

	@Column(nullable = false, name ="end_date")
	private LocalDateTime endDate;

	@Column(name = "created_at")
	@CreationTimestamp
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	@UpdateTimestamp
	private LocalDateTime updatedAt;

	public void rejectScheduleOverDate() {
		this.state = State.REJECT;
	}
}
