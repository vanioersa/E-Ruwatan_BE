package ERuwatan.Tugasbe.service;

import ERuwatan.Tugasbe.dto.SiswaDTO;
import ERuwatan.Tugasbe.model.SiswaModel;
import ERuwatan.Tugasbe.repository.SiswaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SiswaService {
    private final SiswaRepository siswaRepository;

    @Autowired
    public SiswaService(SiswaRepository siswaRepository) {
        this.siswaRepository = siswaRepository;
    }

    public SiswaDTO createSiswa(SiswaDTO siswaDTO) {
        SiswaModel siswa = new SiswaModel();
        BeanUtils.copyProperties(siswaDTO, siswa);
        siswa = siswaRepository.save(siswa);
        return convertToDTO(siswa);
    }

    public SiswaDTO getSiswaById(Long id) {
        Optional<SiswaModel> siswaOptional = siswaRepository.findById(id);
        return siswaOptional.map(this::convertToDTO).orElse(null);
    }

    public List<SiswaDTO> getAllSiswa() {
        return siswaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public SiswaDTO updateSiswa(Long id, SiswaDTO siswaDTO) {
        Optional<SiswaModel> optionalSiswa = siswaRepository.findById(id);
        if (optionalSiswa.isPresent()) {
            SiswaModel siswa = optionalSiswa.get();
            BeanUtils.copyProperties(siswaDTO, siswa, "id");
            siswa = siswaRepository.save(siswa);
            return convertToDTO(siswa);
        }
        return null;
    }

    public void deleteSiswa(Long id) {
        siswaRepository.deleteById(id);
    }

    private SiswaDTO convertToDTO(SiswaModel siswa) {
        SiswaDTO siswaDTO = new SiswaDTO();
        BeanUtils.copyProperties(siswa, siswaDTO);
        return siswaDTO;
    }
}
