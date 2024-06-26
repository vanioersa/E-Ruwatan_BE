package ERuwatan.Tugasbe.Excell;

import ERuwatan.Tugasbe.model.*;
import ERuwatan.Tugasbe.repository.GuruRepo;
import ERuwatan.Tugasbe.repository.UserRepository;
import javassist.NotFoundException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

@Service
public class ExcelGuruSer {

    @Autowired
    private GuruRepo guruRepo;

    @Autowired
    private UserRepository userRepository;

    public void excelExportGuru(HttpServletResponse response) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Export-Guru");

        List<UserModel> userModelList = userRepository.findAll(); // Adjust this according to your repository method

        int rowNum = 0;

        Row headerRow = sheet.createRow(rowNum++);
        String[] headers = {"ID", "Nama Guru", "Email", "Jenis Kelamin", "Alamat", "Nomor Telepon", "Status Pernikahan"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        for (UserModel guru : userModelList) {
            Row row = sheet.createRow(rowNum++);
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(guru.getId());

            Cell cell1 = row.createCell(1);
            cell1.setCellValue(guru.getUsername());

            Cell cell2 = row.createCell(2);
            cell2.setCellValue(guru.getEmail());

            Cell cell3 = row.createCell(3);
            cell3.setCellValue(guru.getGender());

            Cell cell4 = row.createCell(4);
            cell4.setCellValue(guru.getAlamat());

            Cell cell5 = row.createCell(5);
            cell5.setCellValue(guru.getTelepon());

            Cell cell6 = row.createCell(6);
            cell6.setCellValue(guru.getStatus_nikah());
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=ExportGuru.xlsx");
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    public void importGuruFromExcel(MultipartFile file) throws IOException {
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
                UserModel guru = new UserModel();

                Cell cell0 = currentRow.getCell(0);
                if (cell0 != null && cell0.getCellType() == CellType.NUMERIC) {
                    guru.setId((long) cell0.getNumericCellValue());
                }

                Cell cell1 = currentRow.getCell(1);
                if (cell1 != null && cell1.getCellType() == CellType.STRING) {
                    guru.setUsername(cell1.getStringCellValue());
                }

                Cell cell2 = currentRow.getCell(2);
                if (cell2 != null && cell2.getCellType() == CellType.STRING) {
                    guru.setEmail(cell2.getStringCellValue());
                }

                Cell cell3 = currentRow.getCell(3);
                if (cell3 != null && cell3.getCellType() == CellType.STRING) {
                    guru.setGender(cell3.getStringCellValue());
                }

                Cell cell4 = currentRow.getCell(4);
                if (cell4 != null && cell4.getCellType() == CellType.STRING) {
                    guru.setAlamat(cell4.getStringCellValue());
                }

                Cell cell5 = currentRow.getCell(5);
                if (cell5 != null && cell5.getCellType() == CellType.STRING) {
                    guru.setTelepon(cell5.getStringCellValue());
                }

                Cell cell6 = currentRow.getCell(6);
                if (cell6 != null && cell6.getCellType() == CellType.STRING) {
                    guru.setStatus_nikah(cell6.getStringCellValue());
                }

                userRepository.save(guru);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to import data from Excel file", e);
        }
    }

    public void excelDownloadGuruTemplate(HttpServletResponse response) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Guru Template");

        int rowNum = 0;

        Row headerRow = sheet.createRow(rowNum++);
        String[] headers = {"ID", "Nama Guru", "Email", "Jenis Kelamin", "Alamat", "Nomor Telepon", "Status Pernikahan"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=TemplatePenilaian.xlsx");
        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
