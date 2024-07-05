package ERuwatan.Tugasbe.repository;

import ERuwatan.Tugasbe.model.Piket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PiketRepo extends JpaRepository<Piket, Long>{
    @Query(value = "SELECT * FROM piket WHERE kelas_id = :kelas_id" , nativeQuery = true)
    Optional<Piket>findBykelas(Long kelas_id);

    @Query(value = "SELECT * FROM piket WHERE status = :statusId", nativeQuery = true)
    List<Piket> findByStatusId(Long statusId);

    @Query(value = "SELECT * FROM piket WHERE tanggal = :tanggal AND kelas_id = :kelas_id", nativeQuery = true)
    List<Piket> findByTanggalAndKelasId(String tanggal, Long kelas_id);
}
