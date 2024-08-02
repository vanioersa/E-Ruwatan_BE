package ERuwatan.Tugasbe.service;

import ERuwatan.Tugasbe.dto.KelasDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface KelasSer {
    KelasDTO createKelas(KelasDTO kelasDTO);
    KelasDTO getKelasById(Long id);
    List<KelasDTO> getAllKelas();
    KelasDTO updateKelas(Long id, KelasDTO kelasDTO);
    void deleteKelas(Long id);
}