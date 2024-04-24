package ERuwatan.Tugasbe.service;

import ERuwatan.Tugasbe.model.GuruModel;
import ERuwatan.Tugasbe.model.KelasModel;
import ERuwatan.Tugasbe.repository.KelasRepository;
import ERuwatan.Tugasbe.repository.GuruRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuruService {
    @Autowired
    private GuruRepository guruRepository;

    @Autowired
    private KelasRepository kelasRepository;

    public List<GuruModel> getAllGuru() {
        return guruRepository.findAll();
    }

    public GuruModel createGuru(GuruModel guru) {
        validateAndSetKelas(guru, guru.getKelasModel().getId());
        return guruRepository.save(guru);
    }

    public GuruModel getGuruById(Long id) {
        return guruRepository.findById(id).orElseThrow(() -> new RuntimeException("Guru not found"));
    }

    public GuruModel updateGuru(Long id, GuruModel updatedGuru) {
        GuruModel existingGuru = guruRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Guru not found"));

        existingGuru.setNama_guru(updatedGuru.getNama_guru());
        validateAndSetKelas(existingGuru, updatedGuru.getKelasModel().getId());

        return guruRepository.save(existingGuru);
    }

    public void deleteById(Long id) {
        guruRepository.deleteById(id);
    }

    private void validateAndSetKelas(GuruModel guru, Long kelasId) {
        KelasModel kelas = kelasRepository.findById(kelasId)
                .orElseThrow(() -> new RuntimeException("Kelas not found"));
        guru.setKelasModel(kelas);
    }
}
