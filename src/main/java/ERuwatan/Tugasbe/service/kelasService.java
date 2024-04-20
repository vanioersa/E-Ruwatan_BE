package ERuwatan.Tugasbe.service;

import ERuwatan.Tugasbe.model.KelasModel;
import ERuwatan.Tugasbe.repository.kelasRepository;
import ERuwatan.Tugasbe.dto.kelasDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class kelasService {
    private final kelasRepository kelasRepository;

    @Autowired
    public kelasService(kelasRepository kelasRepository) {
        this.kelasRepository = kelasRepository;
    }

    public kelasDTO createKelas(kelasDTO kelasDTO) {
        KelasModel kelas = new KelasModel();
        BeanUtils.copyProperties(kelasDTO, kelas);
        kelas = kelasRepository.save(kelas);
        return convertToDTO(kelas);
    }

    public kelasDTO getKelasById(Long id) {
        Optional<KelasModel> kelasOptional = kelasRepository.findById(id);
        return kelasOptional.map(this::convertToDTO).orElse(null);
    }

    public List<kelasDTO> getAllKelas() {
        return kelasRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public kelasDTO updateKelas(Long id, kelasDTO kelasDTO) {
        Optional<KelasModel> optionalKelas = kelasRepository.findById(id);
        if (optionalKelas.isPresent()) {
            KelasModel kelas = optionalKelas.get();
            BeanUtils.copyProperties(kelasDTO, kelas, "id");
            kelas = kelasRepository.save(kelas);
            return convertToDTO(kelas);
        }
        return null;
    }

    public void deleteKelas(Long id) {
        kelasRepository.deleteById(id);
    }

    private kelasDTO convertToDTO(KelasModel kelas) {
        kelasDTO kelasDTO = new kelasDTO();
        BeanUtils.copyProperties(kelas, kelasDTO);
        return kelasDTO;
    }
}
