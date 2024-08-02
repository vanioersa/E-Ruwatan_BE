package ERuwatan.Tugasbe.serlmpl;

import ERuwatan.Tugasbe.dto.KelasDTO;
import ERuwatan.Tugasbe.model.Kelas;
import ERuwatan.Tugasbe.repository.KelasRepo;
import ERuwatan.Tugasbe.service.KelasSer;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class KelasSerImpl implements KelasSer {
    @Autowired
    private KelasRepo kelasRepo;

    @Override
    public KelasDTO createKelas(KelasDTO kelasDTO) {
        Kelas kelas = new Kelas();
        BeanUtils.copyProperties(kelasDTO, kelas);
        return convertToDTO(kelasRepo.save(kelas));
    }

    @Override
    public KelasDTO getKelasById(Long id) {
        Optional<Kelas> kelasOptional = kelasRepo.findById(id);
        return kelasOptional.map(this::convertToDTO).orElse(null);
    }

    @Override
    public List<KelasDTO> getAllKelas() {
        return kelasRepo.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public KelasDTO updateKelas(Long id, KelasDTO kelasDTO) {
        Optional<Kelas> optionalKelas = kelasRepo.findById(id);
        if (optionalKelas.isPresent()) {
            Kelas kelas = optionalKelas.get();
            BeanUtils.copyProperties(kelasDTO, kelas);
            kelas.setId(id);
            return convertToDTO(kelasRepo.save(kelas));
        }
        return null;
    }

    @Override
    public void deleteKelas(Long id) {
        kelasRepo.deleteById(id);
    }

    private KelasDTO convertToDTO(Kelas kelas) {
        KelasDTO kelasDTO = new KelasDTO();
        BeanUtils.copyProperties(kelas, kelasDTO);
        return kelasDTO;
    }
}