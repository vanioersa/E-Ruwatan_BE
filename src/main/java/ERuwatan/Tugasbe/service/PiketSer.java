package ERuwatan.Tugasbe.service;

import ERuwatan.Tugasbe.dto.PiketDTO;
import ERuwatan.Tugasbe.dto.SiswaDTO;

import java.util.List;

public interface PiketSer {
    PiketDTO createPiket(PiketDTO piketDTO);
    PiketDTO getPiketById(Long id);
    PiketDTO updatePiket(Long id, PiketDTO piketDTO);
    void deletePiket(Long id);
    List<SiswaDTO> getStudentsByClass(Long kelasId);
    void updateStudentStatus(List<PiketDTO> piketDTOList);
    PiketDTO addSiswaToPiket(PiketDTO piketDTO);
    List<PiketDTO> getPiketByDateAndClass(String tanggal, Long kelasId);
    boolean deletePiketByDateAndClass(String tanggal, Long kelasId);
    List<PiketDTO> getAllPiket();
    List<PiketDTO> getPiketByKelas(Long kelasId);
}
