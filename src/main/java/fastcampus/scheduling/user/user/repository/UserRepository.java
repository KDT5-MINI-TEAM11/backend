package fastcampus.scheduling.user.user.repository;

import fastcampus.scheduling.user.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
