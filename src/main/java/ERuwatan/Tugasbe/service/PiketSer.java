package ERuwatan.Tugasbe.service;

import ERuwatan.Tugasbe.dto.PiketDTO;
import java.util.List;

public interface PiketSer {
    PiketDTO createPiket(PiketDTO piketDTO);
    PiketDTO getPiketById(Long id);
    List<PiketDTO> getAllPikets();
    PiketDTO updatePiket(Long id, PiketDTO piketDTO);
    void deletePiket(Long id);
}
