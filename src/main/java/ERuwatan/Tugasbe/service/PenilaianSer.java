package ERuwatan.Tugasbe.service;

import ERuwatan.Tugasbe.dto.PenilaianDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PenilaianSer {
    PenilaianDTO createPenilaian(PenilaianDTO penilaianDTO);
    PenilaianDTO getPenilaianById(Long id);
    List<PenilaianDTO> getAllPenilaian();
    PenilaianDTO updatePenilaian(Long id, PenilaianDTO penilaianDTO);
    void deletePenilaian(Long id);
    void importData(MultipartFile file) throws IOException;
}