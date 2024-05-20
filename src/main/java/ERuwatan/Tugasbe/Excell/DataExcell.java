package ERuwatan.Tugasbe.Excell;

import ERuwatan.Tugasbe.dto.KelasDTO;
import ERuwatan.Tugasbe.model.Piket;
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
    public static ByteArrayInputStream dataToExcel(List<Piket> dataList) {
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

    public static List<Piket> exceltoData(InputStream is) {

        try {
            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();

            List<Piket> dataList = new ArrayList<Piket>();
            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();

                Piket data = new Piket();

                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellIdx) {
                        case 0:
                            data.setId((long) currentCell.getNumericCellValue());
                            break;
                        case 1:
                            data.setTanggal(currentCell.getStringCellValue());
                            break;
                        case 2:
                            data.setStatus(currentCell.getStringCellValue());
                            break;
                        default:
                            break;

                    }
                    cellIdx++;

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