package ERuwatan.Tugasbe.Excell;

import ERuwatan.Tugasbe.dto.KelasDTO;
import ERuwatan.Tugasbe.dto.SiswaDTO;
import ERuwatan.Tugasbe.model.Piket;
import ERuwatan.Tugasbe.model.Siswa;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DataExcell {

    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    static String[] HEADER_DATA = { "No" ,"tanggal", "status", "kelas", "siswa"};

    static String SHEET = "Sheet1";

    public static boolean hasExcelFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }
    public static ByteArrayInputStream piketanToExcel(List<Piket> dataList) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            Sheet sheet = workbook.createSheet(SHEET);

            Row headerRow = sheet.createRow(0);

            for (int col = 0; col < HEADER_DATA.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADER_DATA[col]);
            }

            int rowIdx = 1;
            for (Piket datas : dataList) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(datas.getId());
                row.createCell(1).setCellValue(datas.getTanggal());
                row.createCell(2).setCellValue(datas.getStatus());
                row.createCell(3).setCellValue(datas.getKelasId().getKelas());
                row.createCell(4).setCellValue(datas.getSiswaId().getNama_siswa());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        }
    }

    public static List<Piket> excelPiketan(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheet(SHEET);

            List<Piket> dataList = new ArrayList<>();

            for (int rowIdx = 1; rowIdx <= sheet.getLastRowNum(); rowIdx++) {
                Row currentRow = sheet.getRow(rowIdx);
                if (currentRow == null) {
                    continue;
                }

                Piket data = new Piket();

                Cell idCell = currentRow.getCell(0);
                if (idCell != null) {
                    data.setId((long) idCell.getNumericCellValue());
                }

                Cell tanggalCell = currentRow.getCell(1);
                if (tanggalCell != null) {
                    data.setTanggal(tanggalCell.getStringCellValue());
                }

                Cell statusCell = currentRow.getCell(2);
                if (statusCell != null) {
                    data.setStatus(statusCell.getStringCellValue());
                }

                Cell kelasCell = currentRow.getCell(3);
                if (kelasCell != null) {
                    KelasDTO kelasDTO = new KelasDTO();
                    kelasDTO.setKelas(kelasCell.getStringCellValue());
                    data.setKelasId(kelasDTO);
                }

                Cell siswaCell = currentRow.getCell(4);
                if (siswaCell != null) {
                    SiswaDTO siswaDTO = new SiswaDTO();
                    siswaDTO.setNama_siswa(siswaCell.getStringCellValue());
                    data.setSiswaId(siswaDTO);
                }

                dataList.add(data);
            }

            workbook.close();
            return dataList;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }

}