package ERuwatan.Tugasbe.Excell;

import ERuwatan.Tugasbe.model.*;
import ERuwatan.Tugasbe.repository.UserRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class ExcelUserSer {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public void excelExportGuru(HttpServletResponse response) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Export-Guru");

        List<UserModel> userModelList = userRepository.findAll(); // Adjust this according to your repository method

        int rowNum = 0;

        Row headerRow = sheet.createRow(rowNum++);
        String[] headers = {"No", "Nama Guru", "Email", "Jenis Kelamin", "Alamat", "Nomor Telepon", "Status Pernikahan", "Role", "Jabatan", "Kelas"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        int number = 1;
        for (UserModel guru : userModelList) {
            if ("GURU".equalsIgnoreCase(guru.getRole())) {
                Row row = sheet.createRow(rowNum++);
                Cell cell0 = row.createCell(0);
                cell0.setCellValue(number++);

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
                cell8.setCellValue(guru.getJabatan());

                Cell cell9 = row.createCell(9);
                if (guru.getKelas() != null) {
                    String kelas = guru.getKelas().getKelas();
                    String namaKelas = guru.getKelas().getNama_kelas();
                    cell9.setCellValue((kelas != null && !kelas.isEmpty() ? kelas : "-") +
                            " - " +
                            (namaKelas != null && !namaKelas.isEmpty() ? namaKelas : "-"));
                } else {
                    cell9.setCellValue("-");
                }
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

    public Map<String, String> importGuruFromExcel(MultipartFile file) throws IOException {
        Map<String, String> passwordMap = new HashMap<>();
        Set<String> existingUsernames = new HashSet<>();
        Set<String> existingEmails = new HashSet<>();
        Map<String, String> errorMessages = new HashMap<>();

        List<UserModel> existingUsers = userRepository.findAll();
        for (UserModel user : existingUsers) {
            existingUsernames.add(user.getUsername());
            existingEmails.add(user.getEmail());
        }

        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            if (rows.hasNext()) {
                rows.next();
            }

            while (rows.hasNext()) {
                Row currentRow = rows.next();
                String username = getCellValueAsString(currentRow.getCell(1));
                String email = getCellValueAsString(currentRow.getCell(2));

                if (username == null || email == null) {
                    continue;
                }

                if (existingUsernames.contains(username)) {
                    errorMessages.put(username, "Username '" + username + "' sudah ada.");
                    continue;
                }

                if (existingEmails.contains(email)) {
                    errorMessages.put(email, "Email '" + email + "' sudah ada.");
                    continue;
                }

                UserModel guru = new UserModel();
                guru.setUsername(username);
                guru.setEmail(email);
                guru.setGender(getCellValueAsString(currentRow.getCell(3)));
                guru.setAlamat(getCellValueAsString(currentRow.getCell(4)));
                guru.setTelepon(getCellValueAsString(currentRow.getCell(5)));
                guru.setStatus_nikah(getCellValueAsString(currentRow.getCell(6)));
                guru.setRole(getCellValueAsString(currentRow.getCell(7)));
                guru.setJabatan(getCellValueAsString(currentRow.getCell(8)));

                String password = getCellValueAsString(currentRow.getCell(9));
                if (password == null || password.isEmpty()) {
                    password = generateRandomPassword();
                }
                guru.setPassword(passwordEncoder.encode(password));

                if ("GURU".equalsIgnoreCase(guru.getRole())) {
                    userRepository.save(guru);
                    passwordMap.put(username, password);
                    existingUsernames.add(username);
                    existingEmails.add(email);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Gagal mengimpor data dari file Excel", e);
        }

        if (!errorMessages.isEmpty()) {
            throw new IllegalArgumentException("Gagal mengimpor data: " + errorMessages);
        }

        return passwordMap;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return null;
        }
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf((int) cell.getNumericCellValue());
        } else {
            return null;
        }
    }

    private String generateRandomPassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int length = 8;

        StringBuilder password = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * characters.length());
            password.append(characters.charAt(index));
        }
        return password.toString();
    }

    public void excelDownloadGuruTemplate(HttpServletResponse response) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Guru Template");

        int rowNum = 0;

        Row headerRow = sheet.createRow(rowNum++);
        String[] headers = {"No", "Nama Guru", "Email", "Jenis Kelamin", "Alamat", "Nomor Telepon", "Status Pernikahan", "Role", "Jabatan"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=TemplateGuru.xlsx");
        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
