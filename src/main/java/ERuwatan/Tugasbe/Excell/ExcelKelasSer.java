package ERuwatan.Tugasbe.Excell;

import ERuwatan.Tugasbe.exception.NotFoundException;
import ERuwatan.Tugasbe.model.Kelas;
import ERuwatan.Tugasbe.model.UserModel;
import ERuwatan.Tugasbe.repository.KelasRepo;
import ERuwatan.Tugasbe.repository.UserRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class ExcelKelasSer {

    @Autowired
    private KelasRepo kelasRepo;

    @Autowired
    private UserRepository userRepository;

    public void excelExportKelas(HttpServletResponse response) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Export-Kelas");

        // Corrected the method call to remove the extra parenthesis
        List<Kelas> kelasList = kelasRepo.findAll();

        int rowNum = 0;

        // Creating header
        Row headerRow = sheet.createRow(rowNum++);
        String[] headers = {"ID", "Kelas", "Nama Kelas"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Filling data
        for (Kelas kelas : kelasList) {
            Row row = sheet.createRow(rowNum++);
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(kelas.getId());

            Cell cell1 = row.createCell(1);
            cell1.setCellValue(kelas.getKelas());

            Cell cell2 = row.createCell(2);
            cell2.setCellValue(kelas.getNama_kelas());
        }

        // Adjusting column sizes
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Setting response for Excel file
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=ExportKelas.xlsx");
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    public void importKelasFromExcel(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            // Skip header row
            if (rows.hasNext()) {
                rows.next();
            }

            while (rows.hasNext()) {
                Row currentRow = rows.next();
                Kelas kelas = new Kelas();

                // Assuming columns are ID, Kelas, Nama Kelas
                Cell cell0 = currentRow.getCell(0);
                if (cell0 != null) {
                    kelas.setId((long) cell0.getNumericCellValue());
                }

                Cell cell1 = currentRow.getCell(1);
                if (cell1 != null) {
                    kelas.setKelas(cell1.getStringCellValue());
                }

                Cell cell2 = currentRow.getCell(2);
                if (cell2 != null) {
                    kelas.setNama_kelas(cell2.getStringCellValue());
                }

                // Set admin ID or perform other admin-related actions
                // kelas.setAdminId(adminId); // Uncomment if Kelas has an adminId field

                // Save the Kelas entity to the repository
                kelasRepo.save(kelas);
            }
        }
    }
}
