package fastcampus.scheduling._core.util;

import fastcampus.scheduling.user.common.Position;
import fastcampus.scheduling.user.model.User;
import fastcampus.scheduling.user.repository.UserRepository;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SqlCommandLineRunner implements CommandLineRunner {

	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final UserRepository userRepository;

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
				.createdAt(Timestamp.valueOf(LocalDateTime.now()))
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
				.createdAt(Timestamp.valueOf(LocalDateTime.now()))
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
				.createdAt(Timestamp.valueOf(LocalDateTime.now()))
				.usedVacation(1)
				.build();

		User levelFour = User.builder()
				.id(4L)
				.userEmail("suletta@mercury.com")
				.userName("Suletta Mercury")
				.userPassword(bCryptPasswordEncoder.encode("caliburn1234"))
				.phoneNumber("010-8888-8888")
				.position(Position.MANAGER)
				.profileThumbUrl("https://shorturl.at/actIT")
				.createdAt(Timestamp.valueOf(LocalDateTime.now()))
				.usedVacation(10)
				.build();


		User manager = User.builder()
				.id(5L)
				.userEmail("redcomet3@3xfaster.com")
				.userName("Aznable Char")
				.userPassword(bCryptPasswordEncoder.encode("password1234"))
				.phoneNumber("010-3333-3333")
				.position(Position.LEVEL4)
				.profileThumbUrl("https://shorturl.at/yzCV4")
				.createdAt(Timestamp.valueOf(LocalDateTime.now()))
				.usedVacation(5)
				.build();

		userRepository.save(levelOne);
		userRepository.save(levelTwo);
		userRepository.save(levelThree);
		userRepository.save(levelFour);
		userRepository.save(manager);
	}
}
