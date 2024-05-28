package ERuwatan.Tugasbe.serlmpl;

import ERuwatan.Tugasbe.dto.PenilaianDTO;
import ERuwatan.Tugasbe.model.*;
import ERuwatan.Tugasbe.repository.KelasRepo;
import ERuwatan.Tugasbe.repository.PenilaianRepo;
import ERuwatan.Tugasbe.repository.SiswaRepo;
import ERuwatan.Tugasbe.service.PenilaianSer;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
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

    @Override
    public void importData(MultipartFile file) throws IOException {
        List<PenilaianDTO> penilaianList = parseExcelFile(file.getInputStream());
        for (PenilaianDTO penilaianDTO : penilaianList) {
            createPenilaian(penilaianDTO);
        }
    }

    private List<PenilaianDTO> parseExcelFile(InputStream inputStream) throws IOException {
        List<PenilaianDTO> penilaianList = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rows = sheet.iterator();

        int rowNumber = 0;
        while (rows.hasNext()) {
            Row currentRow = rows.next();

            // Skip header
            if (rowNumber == 0) {
                rowNumber++;
                continue;
            }

            PenilaianDTO penilaianDTO = new PenilaianDTO();

            Cell siswaIdCell = currentRow.getCell(0);
            if (siswaIdCell != null) {
                try {
                    if (siswaIdCell.getCellType() == CellType.NUMERIC) {
                        penilaianDTO.setSiswaId((long) siswaIdCell.getNumericCellValue());
                    } else if (siswaIdCell.getCellType() == CellType.STRING) {
                        penilaianDTO.setSiswaId(Long.parseLong(siswaIdCell.getStringCellValue()));
                    } else {
                        throw new IllegalStateException("Invalid data type for siswaId");
                    }
                } catch (NumberFormatException e) {
                    throw new IllegalStateException("Invalid data format for siswaId: " + siswaIdCell.getStringCellValue(), e);
                }
            }

            Cell kelasIdCell = currentRow.getCell(1);
            if (kelasIdCell != null) {
                try {
                    if (kelasIdCell.getCellType() == CellType.NUMERIC) {
                        penilaianDTO.setKelasId((long) kelasIdCell.getNumericCellValue());
                    } else if (kelasIdCell.getCellType() == CellType.STRING) {
                        String kelasIdString = kelasIdCell.getStringCellValue();
                        if (StringUtils.isNumeric(kelasIdString)) {
                            penilaianDTO.setKelasId(Long.parseLong(kelasIdString));
                        } else {
                            throw new IllegalStateException("Invalid data format for kelasId: " + kelasIdString);
                        }
                    } else {
                        throw new IllegalStateException("Invalid data type for kelasId");
                    }
                } catch (NumberFormatException e) {
                    throw new IllegalStateException("Invalid data format for kelasId: " + kelasIdCell.toString());
                }
            }

            Cell nilaiCell = currentRow.getCell(2);
            if (nilaiCell != null) {
                if (nilaiCell.getCellType() == CellType.NUMERIC) {
                    penilaianDTO.setNilai(String.valueOf((int) nilaiCell.getNumericCellValue()));
                } else if (nilaiCell.getCellType() == CellType.STRING) {
                    penilaianDTO.setNilai(nilaiCell.getStringCellValue());
                } else {
                    throw new IllegalStateException("Invalid data type for nilai");
                }
            }

            Cell deskripsiCell = currentRow.getCell(3);
            if (deskripsiCell != null) {
                if (deskripsiCell.getCellType() == CellType.STRING) {
                    penilaianDTO.setDeskripsi(deskripsiCell.getStringCellValue());
                } else {
                    throw new IllegalStateException("Invalid data type for deskripsi");
                }
            }

            penilaianList.add(penilaianDTO);
        }
        workbook.close();
        return penilaianList;
    }

    private PenilaianDTO convertToDTO(Penilaian penilaian) {
        PenilaianDTO penilaianDTO = new PenilaianDTO();
        BeanUtils.copyProperties(penilaian, penilaianDTO);
        penilaianDTO.setSiswaId(penilaian.getSiswa().getId());
        penilaianDTO.setKelasId(penilaian.getKelas() != null ? penilaian.getKelas().getId() : null);
        return penilaianDTO;
    }
}
