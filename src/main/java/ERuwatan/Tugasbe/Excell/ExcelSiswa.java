package ERuwatan.Tugasbe.Excell;

import ERuwatan.Tugasbe.dto.SiswaDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelSiswa {
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[] HEADERsSiswa = {"NO", "ID", "KELAS ID", "NAMA SISWA", "NISN", "TEMPAT", "ALAMAT"};
    static String[] HEADERsTemplate = {"NO", "ID", "KELAS ID", "NAMA SISWA", "NISN", "TEMPAT", "ALAMAT"};

    static String SHEET = "Sheet1";

    public static boolean hasExcelFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }

    public static ByteArrayInputStream siswaToExcel(List<SiswaDTO> siswas) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet(SHEET);

            Row headerRow = sheet.createRow(0);

            for (int col = 0; col < HEADERsSiswa.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADERsSiswa[col]);
            }

            int rowIdx = 1;
            int no = 1; // Mulai dari 1 untuk nomor urut
            for (SiswaDTO siswa : siswas) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(no++);
                row.createCell(1).setCellValue(siswa.getId());
                row.createCell(2).setCellValue(siswa.getKelasId());
                row.createCell(3).setCellValue(siswa.getNama_siswa());
                row.createCell(4).setCellValue(siswa.getNisn());
                row.createCell(5).setCellValue(siswa.getTempat());
                row.createCell(6).setCellValue(siswa.getAlamat());
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

    public static List<SiswaDTO> excelToSiswa(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();

            List<SiswaDTO> siswaList = new ArrayList<>();
            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();
                SiswaDTO siswa = new SiswaDTO();

                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellIdx) {
                        case 1:
                            siswa.setId((long) currentCell.getNumericCellValue());
                            break;
                        case 2:
                            siswa.setKelasId((long) currentCell.getNumericCellValue());
                            break;
                        case 3:
                            siswa.setNama_siswa(currentCell.getStringCellValue());
                            break;
                        case 4:
                            siswa.setNisn(currentCell.getStringCellValue());
                            break;
                        case 5:
                            siswa.setTempat(currentCell.getStringCellValue());
                            break;
                        case 6:
                            siswa.setAlamat(currentCell.getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    cellIdx++;
                }
                siswaList.add(siswa);
            }
            workbook.close();

            return siswaList;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }
}
