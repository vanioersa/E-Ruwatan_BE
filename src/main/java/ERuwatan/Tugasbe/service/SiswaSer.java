package ERuwatan.Tugasbe.service;

import ERuwatan.Tugasbe.dto.SiswaDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SiswaSer {
    SiswaDTO createSiswa(SiswaDTO siswaDTO);
    SiswaDTO getSiswaById(Long id);
    List<SiswaDTO> getAllSiswas();
    SiswaDTO updateSiswa(Long id, SiswaDTO siswaDTO);
    void deleteSiswa(Long id);
}