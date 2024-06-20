package ERuwatan.Tugasbe.serlmpl;

import ERuwatan.Tugasbe.dto.PenilaianDTO;
import ERuwatan.Tugasbe.exception.NotFoundException;
import ERuwatan.Tugasbe.model.*;
import ERuwatan.Tugasbe.repository.KelasRepo;
import ERuwatan.Tugasbe.repository.PenilaianRepo;
import ERuwatan.Tugasbe.repository.SiswaRepo;
import ERuwatan.Tugasbe.service.PenilaianSer;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
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

//    @Override
//    public void importData(MultipartFile file) throws IOException {
//        List<PenilaianDTO> penilaianList = parseExcelFile(file.getInputStream());
//        for (PenilaianDTO penilaianDTO : penilaianList) {
//            createPenilaian(penilaianDTO);
//        }
//    }

//    private List<PenilaianDTO> parseExcelFile(InputStream inputStream) throws IOException {
//        List<PenilaianDTO> penilaianList = new ArrayList<>();
//        Workbook workbook = new XSSFWorkbook(inputStream);
//        Sheet sheet = workbook.getSheetAt(0);
//        Iterator<Row> rows = sheet.iterator();
//
//        // Skip header row
//        if (rows.hasNext()) {
//            rows.next();
//        }
//
//        while (rows.hasNext()) {
//            Row currentRow = rows.next();
//            PenilaianDTO penilaianDTO = new PenilaianDTO();
//
//            // Kelas ID
//            Cell kelasIdCell = currentRow.getCell(1);
//            if (kelasIdCell != null) {
//                parseKelasIdCell(penilaianDTO, kelasIdCell);
//            }
//
//            // Nilai
//            Cell nilaiCell = currentRow.getCell(2);
//            if (nilaiCell != null) {
//                parseNilaiCell(penilaianDTO, nilaiCell);
//            }
//
//            // Deskripsi
//            Cell deskripsiCell = currentRow.getCell(3);
//            if (deskripsiCell != null) {
//                parseDeskripsiCell(penilaianDTO, deskripsiCell);
//            }
//
//            penilaianList.add(penilaianDTO);
//        }
//        workbook.close();
//        return penilaianList;
//    }

    private void parseKelasIdCell(PenilaianDTO penilaianDTO, Cell kelasIdCell) {
        switch (kelasIdCell.getCellType()) {
            case NUMERIC:
                penilaianDTO.setKelasId((long) kelasIdCell.getNumericCellValue());
                break;
            case STRING:
                String kelasIdString = kelasIdCell.getStringCellValue();
                if (StringUtils.isNumeric(kelasIdString)) {
                    penilaianDTO.setKelasId(Long.parseLong(kelasIdString));
                } else {
                    throw new IllegalStateException("Invalid data format for kelasId: " + kelasIdString);
                }
                break;
            default:
                throw new IllegalStateException("Invalid data type for kelasId");
        }
    }

    private void parseNilaiCell(PenilaianDTO penilaianDTO, Cell nilaiCell) {
        switch (nilaiCell.getCellType()) {
            case NUMERIC:
                penilaianDTO.setNilai(String.valueOf((int) nilaiCell.getNumericCellValue()));
                break;
            case STRING:
                penilaianDTO.setNilai(nilaiCell.getStringCellValue());
                break;
            default:
                throw new IllegalStateException("Invalid data type for nilai");
        }
    }

    private void parseDeskripsiCell(PenilaianDTO penilaianDTO, Cell deskripsiCell) {
        if (deskripsiCell.getCellType() == CellType.STRING) {
            penilaianDTO.setDeskripsi(deskripsiCell.getStringCellValue());
        } else {
            throw new IllegalStateException("Invalid data type for deskripsi");
        }
    }

    private PenilaianDTO convertToDTO(Penilaian penilaian) {
        PenilaianDTO penilaianDTO = new PenilaianDTO();
        BeanUtils.copyProperties(penilaian, penilaianDTO);
        penilaianDTO.setSiswaId(penilaian.getSiswa().getId());
        penilaianDTO.setKelasId(penilaian.getKelas() != null ? penilaian.getKelas().getId() : null);
        return penilaianDTO;
    }
}
