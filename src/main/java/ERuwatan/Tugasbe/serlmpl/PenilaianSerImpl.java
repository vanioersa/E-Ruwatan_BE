package ERuwatan.Tugasbe.serlmpl;

import ERuwatan.Tugasbe.dto.PenilaianDTO;
import ERuwatan.Tugasbe.exception.NotFoundException;
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
    public List<Penilaian> getAllPenilaian() {
        return penilaianRepo.findAll();
    }

    @Override
    public Penilaian updatePenilaian(Long id, PenilaianDTO penilaianDTO) {
     Penilaian update = penilaianRepo.findById(id).orElseThrow(()-> new NotFoundException("Id Not Found"));
     update.setDeskripsi(penilaianDTO.getDeskripsi());
     update.setKelas(kelasRepo.findById(penilaianDTO.getKelasId()).orElseThrow(()-> new NotFoundException("Id Kelas Not Found")));
     update.setNilai(penilaianDTO.getNilai());
     update.setSiswa(siswaRepo.findById(penilaianDTO.getSiswaId()).orElseThrow(()-> new NotFoundException("Id Siswa Not Found ")));
     return penilaianRepo.save(update);
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
