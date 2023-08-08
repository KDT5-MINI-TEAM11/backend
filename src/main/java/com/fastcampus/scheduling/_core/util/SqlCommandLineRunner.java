package com.fastcampus.scheduling._core.util;

import com.fastcampus.scheduling.schedule.repository.ScheduleRepository;
import com.fastcampus.scheduling.user.common.Position;
import com.fastcampus.scheduling.user.model.User;
import com.fastcampus.scheduling.user.repository.UserRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SqlCommandLineRunner implements CommandLineRunner {

	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final UserRepository userRepository;
	private final ScheduleRepository scheduleRepository;

	@Value("${default-user.password}")
	private String password;

	/**
	 * Temp User For Api Test
	 * Will remove when deploy
	 * @param args incoming main method arguments
	 * @throws Exception
	 */
	@Override
	public void run(String... args) {


		try {
			User levelOne = User.builder()
				.userEmail("oregundam@celestial-being.net")
				.userName("Setsuna F Seiei")
				.userPassword(bCryptPasswordEncoder.encode(password))
				.phoneNumber("010-7777-7777")
				.position(Position.LEVEL1)
				.profileThumbUrl("https://shorturl.at/COVY3")
				.createdAt(LocalDateTime.now())
				.usedVacation(0)
				.build();

			User levelTwo = User.builder()
				.userEmail("firstgundam@gundam.net")
				.userName("Amuro Ray")
				.userPassword(bCryptPasswordEncoder.encode(password))
				.phoneNumber("010-2222-2222")
				.position(Position.LEVEL3)
				.profileThumbUrl("https://shorturl.at/qIMPU")
				.createdAt(LocalDateTime.now())
				.usedVacation(8)
				.build();

			User levelThree = User.builder()
				.userEmail("sayla_mass007@gundam.net")
				.userName("Sayla Mass")
				.userPassword(bCryptPasswordEncoder.encode(password))
				.phoneNumber("010-4444-4444")
				.position(Position.LEVEL2)
				.profileThumbUrl("https://shorturl.at/bjqDZ")
				.createdAt(LocalDateTime.now())
				.usedVacation(1)
				.build();

			User levelFour = User.builder()
				.userEmail("suletta@mercury.com")
				.userName("Suletta Mercury")
				.userPassword(bCryptPasswordEncoder.encode(password))
				.phoneNumber("010-8888-8888")
				.position(Position.LEVEL4)
				.profileThumbUrl("https://shorturl.at/actIT")
				.createdAt(LocalDateTime.now())
				.usedVacation(10)
				.build();


			User manager = User.builder()
				.userEmail("redcomet3@3xfaster.com")
				.userName("Aznable Char")
				.userPassword(bCryptPasswordEncoder.encode(password))
				.phoneNumber("010-3333-3333")
				.position(Position.MANAGER)
				.profileThumbUrl("https://shorturl.at/yzCV4")
				.createdAt(LocalDateTime.now())
				.usedVacation(5)
				.build();

			User managerJW = User.builder()
				.userEmail("jw@naver.com")
				.userName("LeeJW")
				.userPassword(bCryptPasswordEncoder.encode(password))
				.phoneNumber("010-3333-0001")
				.position(Position.MANAGER)
				.profileThumbUrl("https://shorturl.at/yzCV4")
				.createdAt(LocalDateTime.now())
				.usedVacation(10)
				.build();

			User managerGH = User.builder()
				.userEmail("gh@naver.com")
				.userName("NamGH")
				.userPassword(bCryptPasswordEncoder.encode(password))
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

		}catch (Exception e){
			log.warn("EXCEPTION OCCURRED IN SQL COMMAND RUNNER");
			log.warn(e.getMessage());
		}

	}
}
