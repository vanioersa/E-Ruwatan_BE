package ERuwatan.Tugasbe.Excell;

import ERuwatan.Tugasbe.model.Kelas;
import ERuwatan.Tugasbe.repository.KelasRepo;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelKelasSer {

    @Autowired
    private KelasRepo kelasRepo;

//    public void excelExportKelas(HttpServletResponse response) throws IOException {
//        Workbook workbook = new XSSFWorkbook();
//        Sheet sheet = workbook.createSheet("Export-Kelas");
//
//        // Corrected the method call to remove the extra parenthesis
//        List<Kelas> kelasList = kelasRepo.findByKelas(kelas);
//
//        int rowNum = 0;
//
//        // Creating header
//        Row headerRow = sheet.createRow(rowNum++);
//        String[] headers = {"ID", "Kelas", "Nama Kelas"};
//        for (int i = 0; i < headers.length; i++) {
//            Cell cell = headerRow.createCell(i);
//            cell.setCellValue(headers[i]);
//        }
//
//        // Filling data
//        for (Kelas kelas : kelasList) {
//            Row row = sheet.createRow(rowNum++);
//            Cell cell0 = row.createCell(0);
//            cell0.setCellValue(kelas.getId());
//
//            Cell cell1 = row.createCell(1);
//            cell1.setCellValue(kelas.getKelas());
//
//            Cell cell2 = row.createCell(2);
//            cell2.setCellValue(kelas.getNama_kelas());
//        }
//
//        // Adjusting column sizes
//        for (int i = 0; i < headers.length; i++) {
//            sheet.autoSizeColumn(i);
//        }
//
//        // Setting response for Excel file
//        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//        response.setHeader("Content-Disposition", "attachment; filename=ExportKelas.xlsx");
//        workbook.write(response.getOutputStream());
//        workbook.close();
//    }
}
