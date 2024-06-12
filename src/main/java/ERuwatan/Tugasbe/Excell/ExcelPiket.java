package ERuwatan.Tugasbe.Excell;

import ERuwatan.Tugasbe.dto.KelasDTO;
import ERuwatan.Tugasbe.dto.SiswaDTO;
import ERuwatan.Tugasbe.model.Piket;
import ERuwatan.Tugasbe.repository.KelasRepo;
import ERuwatan.Tugasbe.repository.SiswaRepo;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class ExcelPiket {
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[] HEADERs = {"tanggal", "status", "kelas", "siswa"};
    static String SHEET = "Sheet1";

    private static KelasRepo kelasRepo;
    private static SiswaRepo siswaRepo;

    @Autowired
    public ExcelPiket(KelasRepo kelasRepo, SiswaRepo siswaRepo) {
        ExcelPiket.kelasRepo = kelasRepo;
        ExcelPiket.siswaRepo = siswaRepo;
    }

    public static boolean hasExcelFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }

    public static ByteArrayInputStream piketToExcel(List<Piket> piketList) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet(SHEET);
            Row headerRow = sheet.createRow(0);

            for (int col = 0; col < HEADERs.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADERs[col]);
            }

            int rowIdx = 1;
            for (Piket piket : piketList) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(piket.getTanggal());
                row.createCell(1).setCellValue(piket.getStatus());
                row.createCell(2).setCellValue(piket.getKelasId().getNama_kelas());
                row.createCell(3).setCellValue(piket.getSiswaId().getNama_siswa());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        }
    }

    public static List<Piket> excelPiket(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();

            List<Piket> piketList = new ArrayList<>();
            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();
                Piket piket = new Piket();
                int cellIdx = 0;

                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellIdx) {
                        case 0:
                            piket.setTanggal(currentCell.getStringCellValue());
                            break;
                        case 1:
                            piket.setStatus(currentCell.getStringCellValue());
                            break;
                        case 2:
                            String kelasName = currentCell.getStringCellValue();
                            Optional<KelasDTO> optionalKelasDTO = kelasRepo.findByNamaKelas(kelasName);
                            if (optionalKelasDTO.isPresent()) {
                                KelasDTO kelasDTO = optionalKelasDTO.get();
                                piket.setKelasId(kelasDTO);
                            } else {
                                throw new EntityNotFoundException("Kelas tidak ditemukan: " + kelasName);
                            }
                            break;
                        case 3:
                            String siswaName = currentCell.getStringCellValue();
                            Optional<SiswaDTO> optionalSiswaDTO = siswaRepo.findBySiswa(siswaName);
                            if (optionalSiswaDTO.isPresent()) {
                                SiswaDTO siswaDTO = optionalSiswaDTO.get();
                                piket.setSiswaId(siswaDTO);
                            } else {
                                throw new EntityNotFoundException("Siswa tidak ditemukan: " + siswaName);
                            }
                            break;
                        default:
                            break;
                    }
                    cellIdx++;
                }

                piketList.add(piket);
            }
            workbook.close();
            return piketList;
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse Excel file: " + e.getMessage());
        }
    }
}
