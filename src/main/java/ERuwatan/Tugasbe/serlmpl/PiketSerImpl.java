package ERuwatan.Tugasbe.serlmpl;

import ERuwatan.Tugasbe.dto.PiketDTO;
import ERuwatan.Tugasbe.model.Piket;
import ERuwatan.Tugasbe.model.Kelas;
import ERuwatan.Tugasbe.model.Siswa;
import ERuwatan.Tugasbe.repository.PiketRepo;
import ERuwatan.Tugasbe.repository.KelasRepo;
import ERuwatan.Tugasbe.repository.SiswaRepo;
import ERuwatan.Tugasbe.service.PiketSer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PiketSerImpl implements PiketSer {

    @Autowired
    private PiketRepo piketRepo;

    @Autowired
    private KelasRepo kelasRepo;

    @Autowired
    private SiswaRepo siswaRepo;

    @Override
    public PiketDTO createPiket(PiketDTO piketDTO) {
        Piket piket = new Piket();
        BeanUtils.copyProperties(piketDTO, piket);
        Optional<Kelas> kelasOptional = kelasRepo.findById(piketDTO.getKelasId());
        kelasOptional.ifPresent(piket::setKelas);
        Optional<Siswa> siswaOptional = siswaRepo.findById(piketDTO.getSiswaId());
        siswaOptional.ifPresent(piket::setSiswa);
        return convertToDTO(piketRepo.save(piket));
    }

    @Override
    public PiketDTO getPiketById(Long id) {
        Optional<Piket> piketOptional = piketRepo.findById(id);
        return piketOptional.map(this::convertToDTO).orElse(null);
    }

    @Override
    public List<PiketDTO> getAllPikets() {
        return piketRepo.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public PiketDTO updatePiket(Long id, PiketDTO piketDTO) {
        Optional<Piket> optionalPiket = piketRepo.findById(id);
        if (optionalPiket.isPresent()) {
            Piket piket = optionalPiket.get();
            BeanUtils.copyProperties(piketDTO, piket);
            Optional<Kelas> kelasOptional = kelasRepo.findById(piketDTO.getKelasId());
            kelasOptional.ifPresent(piket::setKelas);
            Optional<Siswa> siswaOptional = siswaRepo.findById(piketDTO.getSiswaId());
            siswaOptional.ifPresent(piket::setSiswa);
            piket.setId(id);
            return convertToDTO(piketRepo.save(piket));
        }
        return null;
    }

    @Override
    public void deletePiket(Long id) {
        piketRepo.deleteById(id);
    }

    private PiketDTO convertToDTO(Piket piket) {
        PiketDTO piketDTO = new PiketDTO();
        BeanUtils.copyProperties(piket, piketDTO);
        piketDTO.setKelasId(piket.getKelas() != null ? piket.getKelas().getId() : null);
        piketDTO.setSiswaId(piket.getSiswa() != null ? piket.getSiswa().getId() : null);
        return piketDTO;
    }
}
