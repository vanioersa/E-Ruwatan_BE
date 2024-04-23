package ERuwatan.Tugasbe.service;

import ERuwatan.Tugasbe.dto.GuruDTO;
import ERuwatan.Tugasbe.dto.SiswaDTO;
import ERuwatan.Tugasbe.model.GuruModel;
import ERuwatan.Tugasbe.model.KelasModel;
import ERuwatan.Tugasbe.model.SiswaModel;
import ERuwatan.Tugasbe.repository.SiswaRepository;
import ERuwatan.Tugasbe.repository.kelasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SiswaService {
    @Autowired
    private SiswaRepository siswaRepository;

    @Autowired
    private kelasRepository kelasRepository;

    public List<SiswaDTO> getAllSiswa() {
        List<SiswaModel> siswaModels = siswaRepository.findAll();
        return siswaModels.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public SiswaDTO getSiswaById(Long id) {
        SiswaModel siswaModel = siswaRepository.findById(id).orElseThrow(() -> new RuntimeException("Movie not found"));
        return mapToDTO(siswaModel);
    }

    public SiswaDTO createSiswa(SiswaDTO siswaDTO) {
        SiswaModel siswa = new SiswaModel();
        siswa.setNama_siswa(siswaDTO.getNama_siswa());

        Long kelasId = Long.parseLong(siswaDTO.getKelasId());
        KelasModel kelas = kelasRepository.findById(kelasId)
                .orElseThrow(() -> new RuntimeException("Kelas not found"));
        siswa.setKelasModel(kelas);

        siswa = siswaRepository.save(siswa);
        return mapToDTO(siswa);
    }

    public SiswaDTO updateSiswa(Long id, SiswaDTO siswaDTO) {
        SiswaModel siswaModel = siswaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Siswa not found"));
        siswaModel.setNama_siswa(siswaDTO.getNama_siswa());

        Long kelasId = Long.parseLong(siswaDTO.getKelasId());
        KelasModel kelas = kelasRepository.findById(kelasId)
                .orElseThrow(() -> new RuntimeException("Kelas not found"));
        siswaModel.setKelasModel(kelas);

        siswaModel = siswaRepository.save(siswaModel);
        return mapToDTO(siswaModel);
    }

    public void deleteById(Long id) {
        siswaRepository.deleteById(id);
    }

    private SiswaDTO mapToDTO(SiswaModel siswaModel) {
        SiswaDTO dto = new SiswaDTO();
        dto.setId(siswaModel.getId());
        dto.setNama_siswa(siswaModel.getNama_siswa());
        dto.setKelasId(String.valueOf(siswaModel.getKelasModel().getId())); // Assuming ID needs to be stored as String
        dto.setKelasId(siswaModel.getKelasModel().getNama_kelas()); // Assuming you need to keep the class name

        return dto;
    }
}
