package ERuwatan.Tugasbe.service;

import ERuwatan.Tugasbe.dto.KbmDTO;
import java.util.List;

public interface KbmSer {
    KbmDTO createKbm(KbmDTO kbmDTO);
    KbmDTO getKbmById(Long id);
    List<KbmDTO> getAllKbm();
    KbmDTO updateKbm(Long id, KbmDTO kbmDTO);
    void deletKbm(Long id);
}
