package ERuwatan.Tugasbe.serlmpl;

import ERuwatan.Tugasbe.Excell.ExcelPiket;
import ERuwatan.Tugasbe.dto.DpiketDTO;
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
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.*;
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

        List<String> status = piketDTO.getStatus();
        if (status != null && !status.isEmpty()) {
            piket.setStatus(status.get(0));
        }

        List<DpiketDTO> dpiketDTOList = piketDTO.getDpiketDTOList();
        if (dpiketDTOList != null && !dpiketDTOList.isEmpty()) {
            DpiketDTO dpiketDTO = dpiketDTOList.get(0);
            List<Long> siswaId = piketDTO.getSiswaId();
            if (siswaId != null && !siswaId.isEmpty()) {
                List<Siswa> siswaList = new ArrayList<>();
                for (Long id : siswaId) {
                    Optional<Siswa> siswaOptional = siswaRepo.findById(id);
                    siswaOptional.ifPresent(siswaList::add);
                }
                dpiketDTO.setSiswaList(siswaList);
            }
            piket.setDpiketDTOList(Collections.singletonList(dpiketDTO));
        }

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

            PiketDTO dpiketDTO = (PiketDTO) piketDTO.getDpiketDTOList();
            List<Long> siswaId = piketDTO.getSiswaId();
            Optional<Siswa> siswaOptional = siswaRepo.findById(siswaId.get(0)); // Ubah sesuai kebutuhan
            siswaOptional.ifPresent(piket::setSiswa);

            piket.setId(id);
            return convertToDTO(piketRepo.save(piket));
        }
        return null;
    }

    public void deletePiket(Long id) {
        if (piketRepo.existsById(id)) {
            piketRepo.deleteById(id);
        } else {
            throw new EntityNotFoundException("Piket dengan ID " + id + " tidak ditemukan");
        }
    }

    @Override
    public void importPiketan(MultipartFile file) {

    }

    private PiketDTO convertToDTO(Piket piket) {
        PiketDTO piketDTO = new PiketDTO();
        BeanUtils.copyProperties(piket, piketDTO);
        piketDTO.setKelasId(piket.getKelas() != null ? piket.getKelas().getId() : null);
        piketDTO.setSiswaId(Collections.singletonList(piket.getSiswa() != null ? piket.getSiswa().getId() : null));
        return piketDTO;
    }

    public void savePiket(MultipartFile file) {
        try {
            List<Piket> piketList = ExcelPiket.excelPiket(file.getInputStream());
            piketRepo.saveAll(piketList);
        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
    }

}