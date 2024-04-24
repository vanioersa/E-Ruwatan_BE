package ERuwatan.Tugasbe.service;

import ERuwatan.Tugasbe.dto.GuruDTO;
import ERuwatan.Tugasbe.model.GuruModel;
import ERuwatan.Tugasbe.model.KelasModel;
import ERuwatan.Tugasbe.repository.KelasRepository;
import ERuwatan.Tugasbe.repository.GuruRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GuruService {
    @Autowired
    private GuruRepository guruRepository;

    @Autowired
    private KelasRepository kelasRepository;

    public List<GuruDTO> getAllGuru() {
        List<GuruModel> guruModels = guruRepository.findAll();
        return guruModels.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public GuruDTO createGuru(GuruDTO guruDTO) {
        GuruModel guru = new GuruModel();
        guru.setNama_guru(guru.getNama_guru());

        Long kelasId = Long.parseLong(guruDTO.getKelasId());
        KelasModel kelas = kelasRepository.findById(kelasId)
                .orElseThrow(() -> new RuntimeException("Kelas not found"));
        guru.setKelasModel(kelas);

        guru = guruRepository.save(guru);
        return mapToDTO(guru);
    }

    public GuruDTO getGuruById(Long id) {
        GuruModel guruModel = guruRepository.findById(id).orElseThrow(() -> new RuntimeException("Movie not found"));
        return mapToDTO(guruModel);
    }

    public GuruDTO updateGuru(Long id, GuruDTO guruDTO) {
        GuruModel guruModel = guruRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Siswa not found"));
        guruModel.setNama_guru(guruDTO.getNama_guru());

        Long kelasId = Long.parseLong(guruDTO.getKelasId());
        KelasModel kelas = kelasRepository.findById(kelasId)
                .orElseThrow(() -> new RuntimeException("Kelas not found"));
        guruModel.setKelasModel(kelas);

        guruModel = guruRepository.save(guruModel);
        return mapToDTO(guruModel);
    }

    public void deleteById(Long id) {
        guruRepository.deleteById(id);
    }

    private GuruDTO mapToDTO(GuruModel guruModel) {
        GuruDTO dto = new GuruDTO();
        dto.setId(guruModel.getId());
        dto.setNama_guru(guruModel.getNama_guru());
        dto.setNip(guruModel.getNip());
        dto.setMapel(guruModel.getMapel());
        dto.setTempat_lahir(guruModel.getTempat_lahir());
        dto.setKelasId(String.valueOf(guruModel.getKelasModel().getId())); // Assuming ID needs to be stored as String
        dto.setKelasId(guruModel.getKelasModel().getNama_kelas()); // Assuming you need to keep the class name

        return dto;
    }
}