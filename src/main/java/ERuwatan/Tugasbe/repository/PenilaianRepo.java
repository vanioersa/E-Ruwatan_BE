package ERuwatan.Tugasbe.repository;

import ERuwatan.Tugasbe.model.Kbm;
import ERuwatan.Tugasbe.model.Penilaian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PenilaianRepo extends JpaRepository<Penilaian, Long> {
    @Query(value = "SELECT * FROM penilaian WHERE kelas_id = :kelas_id AND siswa_id = :siswa_id", nativeQuery = true)
    List<Penilaian> findByKelasIdAndSiswaId(Long kelas_id, Long siswa_id);
}
