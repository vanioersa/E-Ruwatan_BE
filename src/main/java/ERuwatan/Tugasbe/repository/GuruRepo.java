package ERuwatan.Tugasbe.repository;

import ERuwatan.Tugasbe.model.Guru;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GuruRepo extends JpaRepository<Guru, Long> {
    Optional<Guru> findById(Long id);
}
