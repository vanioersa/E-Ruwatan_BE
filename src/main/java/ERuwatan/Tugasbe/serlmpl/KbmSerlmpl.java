package ERuwatan.Tugasbe.serlmpl;

import ERuwatan.Tugasbe.dto.KbmDTO;
import ERuwatan.Tugasbe.model.Guru;
import ERuwatan.Tugasbe.model.Kbm;
import ERuwatan.Tugasbe.model.Kelas;
import ERuwatan.Tugasbe.repository.GuruRepo;
import ERuwatan.Tugasbe.repository.KbmRepo;
import ERuwatan.Tugasbe.repository.KelasRepo;
import ERuwatan.Tugasbe.service.KbmSer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class KbmSerlmpl implements KbmSer {

    @Autowired
    private GuruRepo guruRepo;

    @Autowired
    private KbmRepo kbmRepo;

    @Autowired
    private KelasRepo kelasRepo;

    @Override
    public KbmDTO createKbm(KbmDTO kbmDTO) {
        Kbm kbm = new Kbm();
        BeanUtils.copyProperties(kbmDTO, kbm);
        Optional<Guru> guruOptional = guruRepo.findById(kbmDTO.getNamaId());
        guruOptional.ifPresent(kbm::setNama);
        Optional<Kelas> kelasOptional = kelasRepo.findById(kbmDTO.getKelasId());
        kelasOptional.ifPresent(kbm::setKelas);
        return convertToDTO(kbmRepo.save(kbm));
    }

    @Override
    public KbmDTO   getKbmById(Long id) {
        Optional<Kbm> kbmOptional = kbmRepo.findById(id);
        return kbmOptional.map(this::convertToDTO).orElse(null);
    }

    @Override
    public List<KbmDTO> getAllKbm() {
        return kbmRepo.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public KbmDTO updateKbm(Long id, KbmDTO kbmDTO) {
        Optional<Kbm> optionalKbm = kbmRepo.findById(id);
        if (optionalKbm.isPresent()) {
            Kbm kbm = optionalKbm.get();
            BeanUtils.copyProperties(kbmDTO, kbm);
            Optional<Guru> guruOptional = guruRepo.findById(kbmDTO.getNamaId());
            guruOptional.ifPresent(kbm::setNama);
            Optional<Kelas> kelasOptional = kelasRepo.findById(kbmDTO.getKelasId());
            kelasOptional.ifPresent(kbm::setKelas);
            kbm.setId(id);
            return convertToDTO(kbmRepo.save(kbm));
        }
        return null;
    }

    @Override
    public void deletKbm(Long id) {
        kbmRepo.deleteById(id);
    }

    private KbmDTO convertToDTO(Kbm kbm) {
        KbmDTO kbmDTO = new KbmDTO();
        BeanUtils.copyProperties(kbm, kbmDTO);
        kbmDTO.setNamaId(kbm.getNama().getId());
        kbmDTO.setKelasId(kbm.getKelas() != null ? kbm.getKelas().getId() : null);
        return kbmDTO;
    }
}