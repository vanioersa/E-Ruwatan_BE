package ERuwatan.Tugasbe.repository;

import ERuwatan.Tugasbe.dto.KelasDTO;
import ERuwatan.Tugasbe.model.Kelas;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KelasRepo extends JpaRepository<Kelas, Long> {

KelasDTO findByNamaKelas (String nama_kelas);

}
