package ERuwatan.Tugasbe.service;

import ERuwatan.Tugasbe.model.KelasModel;
import ERuwatan.Tugasbe.repository.KelasRepository;
import ERuwatan.Tugasbe.dto.KelasDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class kelasService {
    @Autowired
    private KelasRepository kelasRepository;

    public List<KelasDTO> getAllKelas() {
        List<KelasModel> kelasModels = kelasRepository.findAll();
        return kelasModels.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public KelasDTO createKelas(KelasDTO kelasDTO) {
        KelasModel kelas = new KelasModel();
        BeanUtils.copyProperties(kelasDTO, kelas);
        kelas = kelasRepository.save(kelas);
        return mapToDTO(kelas);
    }

    public KelasDTO getKelasById(Long id) {
        KelasModel kelasModel = kelasRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kelas not found with id " + id));
        return mapToDTO(kelasModel);
    }

    public KelasDTO updateKelas(Long id, KelasDTO kelasDTO) {
        Optional<KelasModel> optionalKelas = kelasRepository.findById(id);
        if (optionalKelas.isPresent()) {
            KelasModel kelas = optionalKelas.get();
            BeanUtils.copyProperties(kelasDTO, kelas, "id");
            kelas = kelasRepository.save(kelas);
            return mapToDTO(kelas);
        }
        return null;
    }

    public void deleteKelas(Long id) {
        kelasRepository.deleteById(id);
    }

    private KelasDTO mapToDTO(KelasModel kelasModel) {
        KelasDTO dto = new KelasDTO();
        dto.setId(kelasModel.getId());
        dto.setNama_kelas(kelasModel.getNama_kelas());
        dto.setKelas(kelasModel.getKelas());
        // Set other properties as needed
        return dto;
    }

}
