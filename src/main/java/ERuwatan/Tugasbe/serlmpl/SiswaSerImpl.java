package ERuwatan.Tugasbe.serlmpl;

import ERuwatan.Tugasbe.dto.SiswaDTO;
import ERuwatan.Tugasbe.model.Kelas;
import ERuwatan.Tugasbe.model.Siswa;
import ERuwatan.Tugasbe.repository.KelasRepo;
import ERuwatan.Tugasbe.repository.SiswaRepo;
import ERuwatan.Tugasbe.service.SiswaSer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SiswaSerImpl implements SiswaSer {

    @Autowired
    private SiswaRepo siswaRepo;

    @Autowired
    private KelasRepo kelasRepo;

    @Override
    @Transactional
    public SiswaDTO createSiswa(SiswaDTO siswaDTO) {
        Siswa siswa = new Siswa();
        BeanUtils.copyProperties(siswaDTO, siswa);
        Optional<Kelas> kelasOptional = kelasRepo.findById(siswaDTO.getKelasId());
        kelasOptional.ifPresent(siswa::setKelas);
        return convertToDTO(siswaRepo.save(siswa));
    }

    public SiswaDTO getSiswaById(Long id) {
        Optional<Siswa> siswaOptional = siswaRepo.findById(id);
        return siswaOptional.map(this::convertToDTO).orElse(null);
    }

    @Override
    public List<SiswaDTO> getAllSiswas() {
        return siswaRepo.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public SiswaDTO updateSiswa(Long id, SiswaDTO siswaDTO) {
        Optional<Siswa> optionalSiswa = siswaRepo.findById(id);
        if (optionalSiswa.isPresent()) {
            Siswa siswa = optionalSiswa.get();
            BeanUtils.copyProperties(siswaDTO, siswa);
            Optional<Kelas> kelasOptional = kelasRepo.findById(siswaDTO.getKelasId());
            kelasOptional.ifPresent(siswa::setKelas);
            siswa.setId(id);
            return convertToDTO(siswaRepo.save(siswa));
        }
        return null;
    }

    @Override
    public void deleteSiswa(Long id) {
        siswaRepo.deleteById(id);
    }

    public List<Siswa> getSiswaByKelasId(Long id) {
        return siswaRepo.findByKelasId(id);
    }

    private SiswaDTO convertToDTO(Siswa siswa) {
        SiswaDTO siswaDTO = new SiswaDTO();
        BeanUtils.copyProperties(siswa, siswaDTO);
        siswaDTO.setKelasId(siswa.getKelas() != null ? siswa.getKelas().getId() : null);
        return siswaDTO;
    }
}
