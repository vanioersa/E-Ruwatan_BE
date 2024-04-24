package ERuwatan.Tugasbe.service;

import ERuwatan.Tugasbe.dto.KelasDTO;
import ERuwatan.Tugasbe.model.KelasModel;
import ERuwatan.Tugasbe.repository.KelasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class kelasService {

    @Autowired
    private KelasRepository kelasRepository;

    public List<KelasModel> getAllKelas() {
        return kelasRepository.findAll();
    }

    public KelasModel getKelasById(Long id) {
        return kelasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kelas tidak ditemukan"));
    }

    public KelasModel createKelas(KelasModel kelas) {
        return kelasRepository.save(kelas);
    }

    public KelasModel updateKelas(Long id, KelasDTO kelasUpdate) {
        KelasModel kelasExisting = kelasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kelas tidak ditemukan"));
        updateKelasModel(kelasExisting, kelasUpdate);
        return kelasRepository.save(kelasExisting);
    }

    public void deleteKelas(Long id) {
        if (kelasRepository.existsById(id)) {
            kelasRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Class not found");
        }
    }

    private void updateKelasModel(KelasModel kelasExisting, KelasDTO kelasUpdate) {
        // Copy properties from kelasUpdate to kelasExisting, excluding id or other non-updatable fields
        if (kelasUpdate.getNama_kelas() != null) {
            kelasExisting.setNama_kelas(kelasUpdate.getNama_kelas());
        }
        // Add other fields as necessary
    }
}
