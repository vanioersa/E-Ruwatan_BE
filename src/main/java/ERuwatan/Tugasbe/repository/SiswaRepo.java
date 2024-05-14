package ERuwatan.Tugasbe.repository;

import ERuwatan.Tugasbe.model.Siswa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SiswaRepo extends JpaRepository<Siswa, Long> {
    @Query(value = "SELECT * FROM siswa WHERE kelas_id = :kelasId" , nativeQuery = true)
    List<Siswa> findByKelasId(Long kelasId);
}
