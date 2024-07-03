package ERuwatan.Tugasbe.Excell;

import ERuwatan.Tugasbe.model.*;
import ERuwatan.Tugasbe.repository.GuruRepo;
import ERuwatan.Tugasbe.repository.UserRepository;
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
        String[] headers = {"ID", "Nama Guru", "Email", "Jenis Kelamin", "Alamat", "Nomor Telepon", "Status Pernikahan", "Role", "Password"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        for (UserModel guru : userModelList) {
            if ("GURU".equalsIgnoreCase(guru.getRole())) {
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

                Cell cell7 = row.createCell(7);
                cell7.setCellValue(guru.getRole());

                Cell cell8 = row.createCell(8);
                cell8.setCellValue(guru.getPassword()); // Ideally, you should not export passwords directly
            }
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

                // Handle ID (cell 0)
                Cell cell0 = currentRow.getCell(0);
                if (cell0 != null && cell0.getCellType() == CellType.NUMERIC) {
                    guru.setId((long) cell0.getNumericCellValue());
                }

                // Handle Nama Guru (cell 1)
                Cell cell1 = currentRow.getCell(1);
                if (cell1 != null && cell1.getCellType() == CellType.STRING) {
                    guru.setUsername(cell1.getStringCellValue());
                }

                // Handle Email (cell 2)
                Cell cell2 = currentRow.getCell(2);
                if (cell2 != null && cell2.getCellType() == CellType.STRING) {
                    guru.setEmail(cell2.getStringCellValue());
                }

                // Handle Jenis Kelamin (cell 3)
                Cell cell3 = currentRow.getCell(3);
                if (cell3 != null && cell3.getCellType() == CellType.STRING) {
                    guru.setGender(cell3.getStringCellValue());
                }

                // Handle Alamat (cell 4)
                Cell cell4 = currentRow.getCell(4);
                if (cell4 != null && cell4.getCellType() == CellType.STRING) {
                    guru.setAlamat(cell4.getStringCellValue());
                }

                // Handle Nomor Telepon (cell 5)
                Cell cell5 = currentRow.getCell(5);
                if (cell5 != null && cell5.getCellType() == CellType.STRING) {
                    guru.setTelepon(cell5.getStringCellValue());
                }

                // Handle Status Pernikahan (cell 6)
                Cell cell6 = currentRow.getCell(6);
                if (cell6 != null && cell6.getCellType() == CellType.STRING) {
                    guru.setStatus_nikah(cell6.getStringCellValue());
                }

                // Handle Role (cell 7)
                Cell cell7 = currentRow.getCell(7);
                if (cell7 != null && cell7.getCellType() == CellType.STRING) {
                    String role = cell7.getStringCellValue();
                    if ("GURU".equalsIgnoreCase(role)) {
                        guru.setRole(role);

                        // Handle Password (cell 8)
                        Cell cell8 = currentRow.getCell(8);
                        if (cell8 != null && cell8.getCellType() == CellType.STRING) {
                            guru.setPassword(cell8.getStringCellValue()); // Ideally, encrypt the password before saving
                        }

                        userRepository.save(guru);
                    }
                }
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
        String[] headers = {"ID", "Nama Guru", "Email", "Jenis Kelamin", "Alamat", "Nomor Telepon", "Status Pernikahan", "Role", "Password"};
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
