package ERuwatan.Tugasbe.service;
import ERuwatan.Tugasbe.dto.SiswaDTO;
import ERuwatan.Tugasbe.model.KelasModel;
import ERuwatan.Tugasbe.model.SiswaModel;
import ERuwatan.Tugasbe.repository.KelasRepository;
import ERuwatan.Tugasbe.repository.SiswaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class SiswaService {
    @Autowired
    private SiswaRepository siswaRepository;
    @Autowired
    private KelasRepository kelasRepository;
    public List<SiswaDTO> getAllSiswa() {
        List<SiswaModel> siswaModels = siswaRepository.findAll();
        return siswaModels.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public SiswaDTO getSiswaById(Long id) {
        SiswaModel siswaModel = siswaRepository.findById(id).orElseThrow(() -> new RuntimeException("Siswa tidak ditemukan"));
        return mapToDTO(siswaModel);
    }
    public SiswaDTO createSiswa(SiswaDTO siswaDTO) {
        SiswaModel siswa = new SiswaModel();
        mapDTOToSiswaModel(siswaDTO, siswa);
        siswa = siswaRepository.save(siswa);
        return mapToDTO(siswa);
    }
    public SiswaDTO updateSiswa(Long id, SiswaDTO siswaDTO) {
        SiswaModel siswaLama = siswaRepository.findById(id).orElseThrow(() -> new RuntimeException("Siswa tidak ditemukan"));
        mapDTOToSiswaModel(siswaDTO, siswaLama);
        siswaLama = siswaRepository.save(siswaLama);
        return mapToDTO(siswaLama);
    }
    public ResponseEntity<Void> deleteById(Long id) {
        siswaRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    private SiswaDTO mapToDTO(SiswaModel siswaModel) {
        SiswaDTO dto = new SiswaDTO();
        BeanUtils.copyProperties(siswaModel, dto);
        if (siswaModel.getKelasModel() != null) {
            dto.setKelasId(siswaModel.getKelasModel().getId());
        }
        return dto;
    }
    private void mapDTOToSiswaModel(SiswaDTO dto, SiswaModel model) {
        BeanUtils.copyProperties(dto, model);
        if (dto.getKelasId() != null) {
            KelasModel kelas = kelasRepository.findById(dto.getKelasId())
                    .orElseThrow(() -> new RuntimeException("Kelas tidak ditemukan"));
            model.setKelasModel(kelas);
        }
    }
}