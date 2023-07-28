package fastcampus.scheduling.user.repository;

import fastcampus.scheduling.user.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserEmail(String userEmail);
    Optional<User> findByPhoneNumber(String phoneNumber);
}
