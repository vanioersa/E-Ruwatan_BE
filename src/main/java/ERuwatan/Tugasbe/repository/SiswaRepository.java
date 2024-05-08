package ERuwatan.Tugasbe.repository;

import ERuwatan.Tugasbe.model.SiswaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SiswaRepository extends JpaRepository<SiswaModel, Long> {
    @Query(value = "SELECT * FROM Siswa WHERE kelas_id = :kelasId" , nativeQuery = true)
    List<SiswaModel> getSiswaByKelasId(Long kelasId);
}
