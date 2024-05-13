package ERuwatan.Tugasbe.service;

import ERuwatan.Tugasbe.dto.PenilaianDTO;

import java.util.List;

public interface PenilaianSer {
    PenilaianDTO createPenilaian(PenilaianDTO penilaianDTO);
    PenilaianDTO getPenilaianById(Long id);
    List<PenilaianDTO> getAllPenilaian();
    PenilaianDTO updatePenilaian(Long id, PenilaianDTO penilaianDTO);
    void deletePenilaian(Long id);
}
