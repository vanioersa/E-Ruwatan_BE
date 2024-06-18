package ERuwatan.Tugasbe.repository;

import ERuwatan.Tugasbe.dto.KelasDTO;
import ERuwatan.Tugasbe.dto.SiswaDTO;
import ERuwatan.Tugasbe.model.Kelas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface KelasRepo extends JpaRepository<Kelas, Long> {

//KelasDTO findByNamaKelas (String nama_kelas);

    @Query(value = "SELECT * FROM siswa WHERE nama_kelas = :namaKelas" , nativeQuery = true)
    Optional<KelasDTO> findByNamaKelas(String namaKelas);

    List<Kelas> findByKelas(Kelas kelas);

}
