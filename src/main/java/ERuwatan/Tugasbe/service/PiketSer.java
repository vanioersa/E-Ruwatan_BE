package ERuwatan.Tugasbe.service;

import ERuwatan.Tugasbe.dto.KelasDTO;
import ERuwatan.Tugasbe.dto.PiketDTO;
import ERuwatan.Tugasbe.dto.SiswaDTO;

import java.util.List;

public interface PiketSer {
    PiketDTO createPiket(PiketDTO piketDTO);
    PiketDTO getPiketById(Long id);
    boolean deletePiket(Long id);
    List<PiketDTO> getAllPiket();
    SiswaDTO getSiswaById(Long siswaId);
    KelasDTO getKelasById(Long kelasId);
    PiketDTO updatePiket(Long id, PiketDTO piketDTO);
}
