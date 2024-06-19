package ERuwatan.Tugasbe.Excell;

import ERuwatan.Tugasbe.dto.KelasDTO;
import ERuwatan.Tugasbe.dto.SiswaDTO;
import ERuwatan.Tugasbe.model.Penilaian;
import ERuwatan.Tugasbe.model.Piket;
import ERuwatan.Tugasbe.repository.KelasRepo;
import ERuwatan.Tugasbe.repository.SiswaRepo;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class ExcelPenilaian {

    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[] HEADERsPenilaian = {"NO","Nama Siswa", "Kelas", "Nilai Siswa", "Deskripsi"};

    static String[] HEADERsTemplate = {"NO", "ID", "KELAS ID", "NAMA SISWA", "NISN", "TEMPAT", "ALAMAT"};

    static String SHEET = "Sheet1";

    public static boolean hasExcelFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }
    private static KelasRepo kelasRepo;
    private static SiswaRepo siswaRepo;

    public static ByteArrayInputStream penilaianToExcel(List<Penilaian> penilaianList) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet(SHEET);
            Row headerRow = sheet.createRow(0);

            for (int col = 0; col < HEADERsPenilaian.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADERsPenilaian[col]);
            }

            int rowIdx = 1;
            for (Penilaian penilaian : penilaianList) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(1).setCellValue(penilaian.getSiswa().getNama_siswa());
                row.createCell(2).setCellValue(penilaian.getKelas().getNama_kelas());
                row.createCell(3).setCellValue(penilaian.getNilai());
                row.createCell(4).setCellValue(penilaian.getDeskripsi());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        }
    }

    public static ByteArrayInputStream templateToExcel() throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet(SHEET);
            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < HEADERsTemplate.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADERsTemplate[col]);
            }
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        }
    }

//    public static List<Penilaian> excelPenilaian(InputStream is) {
//        try {
//            Workbook workbook = new XSSFWorkbook(is);
//            Sheet sheet = workbook.getSheet(SHEET);
//            Iterator<Row> rows = sheet.iterator();
//
//            List<Penilaian> penilaianList = new ArrayList<>();
//            int rowNumber = 0;
//            while (rows.hasNext()) {
//                Row currentRow = rows.next();
//
//                if (rowNumber == 0) {
//                    rowNumber++;
//                    continue;
//                }
//
//                Iterator<Cell> cellsInRow = currentRow.iterator();
//                Penilaian penilaian = new Penilaian();
//                int cellIdx = 0;
//
//                while (cellsInRow.hasNext()) {
//                    Cell currentCell = cellsInRow.next();
//
//                    switch (cellIdx) {
//
//                        case 3:
//                            String siswaName = currentCell.getStringCellValue();
//                            Optional<SiswaDTO> optionalSiswaDTO = siswaRepo.findBySiswa(siswaName);
//                            if (optionalSiswaDTO.isPresent()) {
//                                SiswaDTO siswaDTO = optionalSiswaDTO.get();
//                                penilaian.setSiswa(siswaDTO);
//                            } else {
//                                throw new EntityNotFoundException("Siswa tidak ditemukan: " + siswaName);
//                            }
//                            break;
//                        case 2:
//                            String kelasName = currentCell.getStringCellValue();
//                            Optional<KelasDTO> optionalKelasDTO = kelasRepo.findByNamaKelas(kelasName);
//                            if (optionalKelasDTO.isPresent()) {
//                                KelasDTO kelasDTO = optionalKelasDTO.get();
//                                penilaian.setKelas(kelasDTO);
//                            } else {
//                                throw new EntityNotFoundException("Kelas tidak ditemukan: " + kelasName);
//                            }
//                            break;
//                        default:
//                            break;
//                        case 1:
//                            penilaian.setNilai(currentCell.getStringCellValue());
//                            penilaian.setDeskripsi(String.valueOf(new Date()));
//                            break;
//                    }
//                    cellIdx++;
//                }
//
//                penilaianList.add(penilaian);
//            }
//            workbook.close();
//            return penilaianList;
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to parse Excel file: " + e.getMessage());
//        }
//    }
}