package ERuwatan.Tugasbe.serlmpl;

import ERuwatan.Tugasbe.dto.PenilaianDTO;
import ERuwatan.Tugasbe.model.*;
import ERuwatan.Tugasbe.repository.KelasRepo;
import ERuwatan.Tugasbe.repository.PenilaianRepo;
import ERuwatan.Tugasbe.repository.SiswaRepo;
import ERuwatan.Tugasbe.service.PenilaianSer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PenilaianSerImpl implements PenilaianSer {
    @Autowired
    private PenilaianRepo penilaianRepo;
    @Autowired
    private SiswaRepo siswaRepo;
    @Autowired
    private KelasRepo kelasRepo;

    @Override
    public PenilaianDTO createPenilaian(PenilaianDTO penilaianDTO) {
        Penilaian penilaian = new Penilaian();
        BeanUtils.copyProperties(penilaianDTO, penilaian);

        Optional<Siswa> siswaOptional = siswaRepo.findById(penilaianDTO.getSiswaId());
        siswaOptional.ifPresent(penilaian::setSiswa);

        Optional<Kelas> kelasOptional = kelasRepo.findById(penilaianDTO.getKelasId());
        kelasOptional.ifPresent(penilaian::setKelas);

        return convertToDTO(penilaianRepo.save(penilaian));
    }

    @Override
    public PenilaianDTO getPenilaianById(Long id) {
        Optional<Penilaian> penilaianOptional = penilaianRepo.findById(id);
        return penilaianOptional.map(this::convertToDTO).orElse(null);
    }

    @Override
    public List<PenilaianDTO> getAllPenilaian() {
        return penilaianRepo.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public PenilaianDTO updatePenilaian(Long id, PenilaianDTO penilaianDTO) {
        Optional<Penilaian> penilaianOptional = penilaianRepo.findById(id);
        if (penilaianOptional.isPresent()) {
            Penilaian penilaian = penilaianOptional.get();
            BeanUtils.copyProperties(penilaianDTO, penilaian);

            Optional<Siswa> siswaOptional = siswaRepo.findById(penilaianDTO.getSiswaId());
            siswaOptional.ifPresent(penilaian::setSiswa);

            Optional<Kelas> kelasOptional = kelasRepo.findById(penilaianDTO.getKelasId());
            kelasOptional.ifPresent(penilaian::setKelas);

            penilaian.setId(id);
            return convertToDTO(penilaianRepo.save(penilaian));
        }
        return null;
    }

    @Override
    public void deletePenilaian(Long id) {
        penilaianRepo.deleteById(id);
    }

    private PenilaianDTO convertToDTO(Penilaian penilaian) {
        PenilaianDTO penilaianDTO = new PenilaianDTO();
        BeanUtils.copyProperties(penilaian, penilaianDTO);
        penilaianDTO.setSiswaId(penilaian.getSiswa().getId());
        penilaianDTO.setKelasId(penilaian.getKelas() != null ? penilaian.getKelas().getId() : null);
        return penilaianDTO;
    }
}
