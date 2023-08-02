package com.fastcampus.scheduling.schedule.common;

import java.util.Objects;

public enum State {
	//temp

	PENDING,
	APPROVE,
	REJECT;

	public static State from(String name) {
		for (State state : State.values()) {
			if (Objects.equals(state.name(), name)) return state;
		}

		throw new RuntimeException();
		//throw new Exception500("권한 매칭 오류");
	}

}
