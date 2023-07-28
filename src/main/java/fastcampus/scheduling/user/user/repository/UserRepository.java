package fastcampus.scheduling.user.user.repository;

import fastcampus.scheduling.user.user.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserEmail(String userEmail);
    Optional<User> findByPhoneNumber(String phoneNumber);
}
