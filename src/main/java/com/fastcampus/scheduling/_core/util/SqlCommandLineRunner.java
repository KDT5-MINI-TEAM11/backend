package com.fastcampus.scheduling._core.util;

import com.fastcampus.scheduling.user.common.Position;
import com.fastcampus.scheduling.user.model.User;
import com.fastcampus.scheduling.user.repository.UserRepository;
import java.nio.file.Files;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SqlCommandLineRunner implements CommandLineRunner {

	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final UserRepository userRepository;
	private final JdbcTemplate jdbcTemplate;

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
				.phoneNumber("01077777777")
				.position(Position.LEVEL1)
				.profileThumbUrl("https://shorturl.at/COVY3")
				.createdAt(LocalDateTime.now())
				.usedVacation(0)
				.build();

			User levelTwo = User.builder()
				.userEmail("firstgundam@gundam.net")
				.userName("Amuro Ray")
				.userPassword(bCryptPasswordEncoder.encode(password))
				.phoneNumber("01022222222")
				.position(Position.LEVEL3)
				.profileThumbUrl("https://shorturl.at/qIMPU")
				.createdAt(LocalDateTime.now())
				.usedVacation(8)
				.build();

			User levelThree = User.builder()
				.userEmail("sayla_mass007@gundam.net")
				.userName("Sayla Mass")
				.userPassword(bCryptPasswordEncoder.encode(password))
				.phoneNumber("01044444444")
				.position(Position.LEVEL2)
				.profileThumbUrl("https://shorturl.at/bjqDZ")
				.createdAt(LocalDateTime.now())
				.usedVacation(1)
				.build();

			User levelFour = User.builder()
				.userEmail("suletta@mercury.com")
				.userName("Suletta Mercury")
				.userPassword(bCryptPasswordEncoder.encode(password))
				.phoneNumber("01088888888")
				.position(Position.LEVEL4)
				.profileThumbUrl("https://shorturl.at/actIT")
				.createdAt(LocalDateTime.now())
				.usedVacation(10)
				.build();


			User manager = User.builder()
				.userEmail("redcomet3@3xfaster.com")
				.userName("Aznable Char")
				.userPassword(bCryptPasswordEncoder.encode(password))
				.phoneNumber("01033333333")
				.position(Position.MANAGER)
				.profileThumbUrl("https://shorturl.at/yzCV4")
				.createdAt(LocalDateTime.now())
				.usedVacation(5)
				.build();

			User managerJW = User.builder()
				.userEmail("jw@naver.com")
				.userName("LeeJW")
				.userPassword(bCryptPasswordEncoder.encode(password))
				.phoneNumber("01033330001")
				.position(Position.MANAGER)
				.profileThumbUrl("https://shorturl.at/yzCV4")
				.createdAt(LocalDateTime.now())
				.usedVacation(10)
				.build();

			User managerGH = User.builder()
				.userEmail("gh@naver.com")
				.userName("NamGH")
				.userPassword(bCryptPasswordEncoder.encode(password))
				.phoneNumber("01033330002")
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

			Resource resource = new ClassPathResource("db/data.sql");
			String sql = new String(Files.readAllBytes(resource.getFile().toPath()));
			jdbcTemplate.execute(sql);


		}catch (Exception e){
			log.warn("EXCEPTION OCCURRED IN SQL COMMAND RUNNER");
			log.warn(e.getMessage());
		}

	}
}
