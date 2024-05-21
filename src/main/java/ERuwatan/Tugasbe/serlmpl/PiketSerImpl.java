package ERuwatan.Tugasbe.serlmpl;

import ERuwatan.Tugasbe.dto.DpiketDTO;
import ERuwatan.Tugasbe.dto.KelasDTO;
import ERuwatan.Tugasbe.dto.PiketDTO;
import ERuwatan.Tugasbe.model.Piket;
import ERuwatan.Tugasbe.model.Kelas;
import ERuwatan.Tugasbe.model.Siswa;
import ERuwatan.Tugasbe.repository.PiketRepo;
import ERuwatan.Tugasbe.repository.KelasRepo;
import ERuwatan.Tugasbe.repository.SiswaRepo;
import ERuwatan.Tugasbe.service.PiketSer;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
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

        DpiketDTO dpiketDTO = (DpiketDTO) piketDTO.getDpiketDTOList();
        List<Long> siswaId = dpiketDTO.getSiswaId();
        Optional<Siswa> siswaOptional = siswaRepo.findById(siswaId.get(0)); // Ubah sesuai kebutuhan
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

            DpiketDTO dpiketDTO = (DpiketDTO) piketDTO.getDpiketDTOList();
            List<Long> siswaId = dpiketDTO.getSiswaId();
            Optional<Siswa> siswaOptional = siswaRepo.findById(siswaId.get(0)); // Ubah sesuai kebutuhan
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

    @Override

    public void importPiketan(MultipartFile file) {
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            // Mulai dari baris kedua, lewati baris header
            int startRow = 1;

            // Iterasi baris
            for (int i = startRow; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);

                // Baca data dari kolom
//                String status = getCellValue(row.getCell(0));
                String tanggal = getCellValue(row.getCell(1));

                // Buat objek KelasDTO
                PiketDTO piketDTO = new PiketDTO();
//                piketDTO.setStatus(status);
                piketDTO.setTanggal(tanggal);

                // Simpan data ke dalam database
                createPiket(piketDTO);
            }
        } catch (IOException e) {
            // Tangani exception jika terjadi kesalahan dalam memproses file
            throw new RuntimeException("Terjadi kesalahan saat memproses file: " + e.getMessage());
        }
    }

    private String getCellValue(Cell cell) {
        if (cell != null) {
            CellType cellType = cell.getCellType();
            if (cellType == CellType.STRING) {
                return cell.getStringCellValue();
            } else if (cellType == CellType.NUMERIC) {
                return String.valueOf((int) cell.getNumericCellValue());
            }
        }
        return "";
    }
}
