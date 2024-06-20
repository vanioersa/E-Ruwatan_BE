package ERuwatan.Tugasbe.repository;

import ERuwatan.Tugasbe.dto.SiswaDTO;
import ERuwatan.Tugasbe.model.Siswa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SiswaRepo extends JpaRepository<Siswa, Long> {
    @Query(value = "SELECT * FROM siswa WHERE kelas_id = :kelasId", nativeQuery = true)
    List<Siswa> findByKelasId(Long kelasId);

    @Query(value = "SELECT * FROM siswa WHERE nama_siswa = :namaSiswa" , nativeQuery = true)
    Optional<SiswaDTO> findBySiswa(String namaSiswa);
}
