package vez.learnauth;

import org.springframework.data.jpa.repository.JpaRepository;
import vez.learnauth.user.UserEntity;

import java.util.Optional;

public interface UserRepo extends JpaRepository<UserEntity, Integer> {

    Optional<UserEntity> findByEmail(String email);
}
