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
    @Autowired
    private kelasRepository kelasRepository;

    public List<kelasDTO> getAllKelas() {
        List<KelasModel> kelasModels = kelasRepository.findAll();
        return kelasModels.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public kelasDTO createKelas(kelasDTO kelasDTO) {
        KelasModel kelas = new KelasModel();
        BeanUtils.copyProperties(kelasDTO, kelas);
        kelas = kelasRepository.save(kelas);
        return mapToDTO(kelas);
    }

    public kelasDTO getKelasById(Long id) {
        KelasModel kelasModel = kelasRepository.findById(id).orElseThrow(() -> new RuntimeException("Movie not found"));
        return mapToDTO(kelasModel);
    }


    public kelasDTO updateKelas(Long id, kelasDTO kelasDTO) {
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

    private kelasDTO mapToDTO(KelasModel kelasModel) {
        kelasDTO dto = new kelasDTO();
        dto.setId(kelasModel.getId());
        dto.setNama_kelas(kelasModel.getNama_kelas());
        dto.setKelas(kelasModel.getKelas());
        // Set other properties as needed
        return dto;
    }

}
