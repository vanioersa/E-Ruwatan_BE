package ERuwatan.Tugasbe.repository;

import ERuwatan.Tugasbe.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
    UserModel findByUsername(String username);
    UserModel findByEmail(String email);
    UserModel findById(long userId);
}
