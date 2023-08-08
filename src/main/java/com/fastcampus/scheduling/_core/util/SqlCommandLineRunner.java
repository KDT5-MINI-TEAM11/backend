package com.fastcampus.scheduling._core.util;

import com.fastcampus.scheduling.schedule.common.ScheduleType;
import com.fastcampus.scheduling.schedule.common.State;
import com.fastcampus.scheduling.schedule.model.Schedule;
import com.fastcampus.scheduling.schedule.repository.ScheduleRepository;
import com.fastcampus.scheduling.user.common.Position;
import com.fastcampus.scheduling.user.model.User;
import com.fastcampus.scheduling.user.repository.UserRepository;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalDateTimeConverter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SqlCommandLineRunner implements CommandLineRunner {

	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final UserRepository userRepository;
	private final ScheduleRepository scheduleRepository;

	/**
	 * Temp User For Api Test
	 * Will remove when deploy
	 * @param args incoming main method arguments
	 * @throws Exception
	 */
	@Override
	public void run(String... args) throws Exception {

		User levelOne = User.builder()
				.id(1L)
				.userEmail("oregundam@celestial-being.net")
				.userName("Setsuna F Seiei")
				.userPassword(bCryptPasswordEncoder.encode("exia1234"))
				.phoneNumber("010-7777-7777")
				.position(Position.LEVEL1)
				.profileThumbUrl("https://shorturl.at/COVY3")
				.createdAt(LocalDateTime.now())
				.usedVacation(0)
				.build();

		User levelTwo = User.builder()
				.id(2L)
				.userEmail("firstgundam@gundam.net")
				.userName("Amuro Ray")
				.userPassword(bCryptPasswordEncoder.encode("first1234"))
				.phoneNumber("010-2222-2222")
				.position(Position.LEVEL3)
				.profileThumbUrl("https://shorturl.at/qIMPU")
				.createdAt(LocalDateTime.now())
				.usedVacation(8)
				.build();

		User levelThree = User.builder()
				.id(3L)
				.userEmail("sayla_mass007@gundam.net")
				.userName("Sayla Mass")
				.userPassword(bCryptPasswordEncoder.encode("gundam5678"))
				.phoneNumber("010-4444-4444")
				.position(Position.LEVEL2)
				.profileThumbUrl("https://shorturl.at/bjqDZ")
				.createdAt(LocalDateTime.now())
				.usedVacation(1)
				.build();

		User levelFour = User.builder()
				.id(4L)
				.userEmail("suletta@mercury.com")
				.userName("Suletta Mercury")
				.userPassword(bCryptPasswordEncoder.encode("caliburn1234"))
				.phoneNumber("010-8888-8888")
				.position(Position.LEVEL4)
				.profileThumbUrl("https://shorturl.at/actIT")
				.createdAt(LocalDateTime.now())
				.usedVacation(10)
				.build();


		User manager = User.builder()
				.id(5L)
				.userEmail("redcomet3@3xfaster.com")
				.userName("Aznable Char")
				.userPassword(bCryptPasswordEncoder.encode("password1234"))
				.phoneNumber("010-3333-3333")
				.position(Position.MANAGER)
				.profileThumbUrl("https://shorturl.at/yzCV4")
				.createdAt(LocalDateTime.now())
				.usedVacation(5)
				.build();

		User managerJW = User.builder()
			.id(6L)
			.userEmail("jw@naver.com")
			.userName("LeeJW")
			.userPassword(bCryptPasswordEncoder.encode("qwer1234!"))
			.phoneNumber("010-3333-0001")
			.position(Position.MANAGER)
			.profileThumbUrl("https://shorturl.at/yzCV4")
			.createdAt(LocalDateTime.now())
			.usedVacation(10)
			.build();

		User managerGH = User.builder()
			.id(7L)
			.userEmail("gh@naver.com")
			.userName("NamGH")
			.userPassword(bCryptPasswordEncoder.encode("qwer1234!"))
			.phoneNumber("010-3333-0002")
			.position(Position.MANAGER)
			.profileThumbUrl("https://shorturl.at/yzCV4")
			.createdAt(LocalDateTime.now())
			.usedVacation(10)
			.build();

		userRepository.save(levelOne);
		userRepository.save(levelTwo);
		userRepository.save(levelThree);
		userRepository.save(levelFour);
		userRepository.save(manager);
		userRepository.save(managerJW);
		userRepository.save(managerGH);

		Schedule schedule1 = Schedule.builder()
			.id(1L)
			.user(levelOne)
			.scheduleType(ScheduleType.ANNUAL)
			.state(State.PENDING)
			.startDate(LocalDateTime.of(2023,8,1,0,0,0))
			.endDate(LocalDateTime.of(2023,8,3,23,59,59))
			.build();
		Schedule schedule2 = Schedule.builder()
			.id(2L)
			.user(levelTwo)
			.scheduleType(ScheduleType.DUTY)
			.state(State.PENDING)
			.startDate(LocalDateTime.of(2023,8,5,0,0,0))
			.endDate(LocalDateTime.of(2023,8,6,23,59,59))
			.build();
		Schedule schedule3 = Schedule.builder()
			.id(3L)
			.user(levelFour)
			.scheduleType(ScheduleType.ANNUAL)
			.state(State.PENDING)
			.startDate(LocalDateTime.of(2023,8,10,0,0,0))
			.endDate(LocalDateTime.of(2023,8,15,23,59,0))
			.build();

		scheduleRepository.save(schedule1);
		scheduleRepository.save(schedule2);
		scheduleRepository.save(schedule3);
	}
}
