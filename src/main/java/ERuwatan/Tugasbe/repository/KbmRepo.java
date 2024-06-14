package ERuwatan.Tugasbe.repository;

import ERuwatan.Tugasbe.model.Kbm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface KbmRepo extends JpaRepository<Kbm, Long> {
    @Query(value = "SELECT * FROM kbm WHERE kelas_id = :kelas_id AND user_id = :user_id", nativeQuery = true)
     List<Kbm> findByKelasIdAndUserId(Long kelas_id, Long user_id);
}
