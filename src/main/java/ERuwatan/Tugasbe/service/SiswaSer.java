package ERuwatan.Tugasbe.service;

import ERuwatan.Tugasbe.dto.SiswaDTO;
import ERuwatan.Tugasbe.model.Siswa;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SiswaSer {
    SiswaDTO createSiswa(SiswaDTO siswaDTO);
    SiswaDTO getSiswaById(Long id);
    List<SiswaDTO> getAllSiswas();
    List<Siswa> getSiswaByKelasId(Long id);
    SiswaDTO updateSiswa(Long id, SiswaDTO siswaDTO);
    void deleteSiswa(Long id);

    void saveSiswa(MultipartFile file);
}