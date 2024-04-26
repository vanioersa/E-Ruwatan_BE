package ERuwatan.Tugasbe.service;

import ERuwatan.Tugasbe.dto.GuruDTO;
import ERuwatan.Tugasbe.model.Guru;
import ERuwatan.Tugasbe.model.Kelas;
import ERuwatan.Tugasbe.repository.KelasRepo;
import ERuwatan.Tugasbe.repository.GuruRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GuruSer {
    @Autowired
    private GuruRepo guruRepository;

    @Autowired
    private KelasRepo kelasRepository;

    public List<GuruDTO> getAllGuru() {
        List<Guru> guruModels = guruRepository.findAll();
        return guruModels.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public GuruDTO createGuru(GuruDTO guruDTO) {
        Guru guru = new Guru();
        guru.setNama_guru(guru.getNama_guru());

        Long kelasId = Long.parseLong(guruDTO.getKelasId());
        Kelas kelas = kelasRepository.findById(kelasId)
                .orElseThrow(() -> new RuntimeException("Kelas not found"));
        guru.setKelasModel(kelas);

        guru = guruRepository.save(guru);
        return mapToDTO(guru);
    }

    public GuruDTO getGuruById(Long id) {
        Guru guruModel = guruRepository.findById(id).orElseThrow(() -> new RuntimeException("Movie not found"));
        return mapToDTO(guruModel);
    }

    public GuruDTO updateGuru(Long id, GuruDTO guruDTO) {
        Guru guruModel = guruRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Siswa not found"));
        guruModel.setNama_guru(guruDTO.getNama_guru());

        Long kelasId = Long.parseLong(guruDTO.getKelasId());
        Kelas kelas = kelasRepository.findById(kelasId)
                .orElseThrow(() -> new RuntimeException("Kelas not found"));
        guruModel.setKelasModel(kelas);

        guruModel = guruRepository.save(guruModel);
        return mapToDTO(guruModel);
    }

    public void deleteById(Long id) {
        guruRepository.deleteById(id);
    }

    private GuruDTO mapToDTO(Guru guruModel) {
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