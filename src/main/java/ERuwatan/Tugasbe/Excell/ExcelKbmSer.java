package ERuwatan.Tugasbe.Excell;

import ERuwatan.Tugasbe.model.Kbm;
import ERuwatan.Tugasbe.model.Kelas;
import ERuwatan.Tugasbe.model.UserModel;
import ERuwatan.Tugasbe.repository.KbmRepo;
import ERuwatan.Tugasbe.repository.KelasRepo;
import ERuwatan.Tugasbe.repository.UserRepository;
import javassist.NotFoundException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
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
public class ExcelKbmSer {

    @Autowired
    private KbmRepo kbmRepo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KelasRepo kelasRepo;

    private String getNameUser(Long user_id) throws NotFoundException {
        Optional<UserModel> userModel = userRepository.findById(user_id);
        if (userModel.isEmpty()) {
            throw new NotFoundException("User ID tidak ditemukan");
        }
        UserModel user = userModel.get();
        return user.getUsername();
    }

    public void excelExportKbm(Long userId, HttpServletResponse response) throws IOException, NotFoundException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Export-Kbm");

        int rowNum = 0;
        Row titleRow = sheet.createRow(rowNum++);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("DATA KBM");

        CellStyle titleStyle = workbook.createCellStyle();
        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 14);
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleCell.setCellStyle(titleStyle);

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));

        Row headerRow = sheet.createRow(rowNum++);
        String[] headers = {"No", "Nama Guru", "Kelas", "Jam Masuk", "Jam Pulang", "Materi", "Keterangan"};

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIME.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        List<Kbm> kbmList;
        if (userId != null) {
            kbmList = kbmRepo.findByUserModelId(userId);
            if (kbmList.isEmpty()) {
                throw new NotFoundException("No data found for user ID: " + userId);
            }
        } else {
            kbmList = kbmRepo.findAll();
            if (kbmList.isEmpty()) {
                throw new NotFoundException("No data found");
            }
        }
        kbmList.sort((u1, u2) -> Long.compare(u2.getId(), u1.getId()));

        int no = 1;
        for (Kbm kbm : kbmList) {
            Row row = sheet.createRow(rowNum++);
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(no++);

            Cell cell1 = row.createCell(1);
            cell1.setCellValue(getNameUser(kbm.getUserModel().getId()));

            Cell cell2 = row.createCell(2);
            cell2.setCellValue(kbm.getKelas().getKelas() + " - " + kbm.getKelas().getNama_kelas());

            Cell cell3 = row.createCell(3);
            cell3.setCellValue(kbm.getJam_masuk());

            Cell cell4 = row.createCell(4);
            cell4.setCellValue(kbm.getJam_pulang());

            Cell cell5 = row.createCell(5);
            cell5.setCellValue(kbm.getMateri());

            Cell cell6 = row.createCell(6);
            cell6.setCellValue(kbm.getKeterangan());
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=ExportKBM.xlsx");
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    public void excelExportAllKbm(HttpServletResponse response) throws IOException, NotFoundException {
        List<Kbm> kbmList = kbmRepo.findAll();

        if (kbmList.isEmpty()) {
            response.setContentType("text/plain");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("No data found to export.");
            return;
        }

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Export-All-Kbm");

        int rowNum = 0;
        Row titleRow = sheet.createRow(rowNum++);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("DATA KBM");

        CellStyle titleStyle = workbook.createCellStyle();
        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 14);
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleCell.setCellStyle(titleStyle);

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));

        Row headerRow = sheet.createRow(rowNum++);
        String[] headers = {"No", "Nama Guru", "Kelas", "Jam Masuk", "Jam Pulang", "Materi", "Keterangan"};

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIME.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        kbmList.sort((u1, u2) -> Long.compare(u2.getId(), u1.getId()));

        int no = 1;
        for (Kbm kbm : kbmList) {
            Row row = sheet.createRow(rowNum++);
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(no++);

            Cell cell1 = row.createCell(1);
            cell1.setCellValue(getNameUser(kbm.getUserModel().getId()));

            Cell cell2 = row.createCell(2);
            cell2.setCellValue(kbm.getKelas().getKelas() + " - " + kbm.getKelas().getNama_kelas());

            Cell cell3 = row.createCell(3);
            cell3.setCellValue(kbm.getJam_masuk());

            Cell cell4 = row.createCell(4);
            cell4.setCellValue(kbm.getJam_pulang());

            Cell cell5 = row.createCell(5);
            cell5.setCellValue(kbm.getMateri());

            Cell cell6 = row.createCell(6);
            cell6.setCellValue(kbm.getKeterangan());
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=ExportAllKBM.xlsx");
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    public void importKBMFromExcel(MultipartFile file, Long userId) throws IOException, NotFoundException {
        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            if (rows.hasNext()) {
                rows.next();
            }

            if (rows.hasNext()) {
                rows.next();
            }

            while (rows.hasNext()) {
                Row currentRow = rows.next();
                Kbm kbm = new Kbm();

                Cell cell1 = currentRow.getCell(1);
                if (cell1 != null) {
                    if (cell1.getCellType() == CellType.STRING) {
                        String username = cell1.getStringCellValue();
                        UserModel user = userRepository.findByUsername(username);
                        if (user == null) {
                            throw new NotFoundException("User dengan username '" + username + "' tidak ditemukan");
                        }
                        if (!userId.equals(user.getId())) {
                            throw new IllegalArgumentException("User ID mismatch");
                        }
                        kbm.setUserModel(user);
                    } else if (cell1.getCellType() == CellType.NUMERIC) {
                        Long userIdFromExcel = (long) cell1.getNumericCellValue();
                        UserModel user = userRepository.findById(userIdFromExcel)
                                .orElseThrow(() -> new NotFoundException("User dengan ID '" + userIdFromExcel + "' tidak ditemukan"));
                        if (!userId.equals(user.getId())) {
                            throw new IllegalArgumentException("User ID mismatch");
                        }
                        kbm.setUserModel(user);
                    } else {
                        throw new IllegalArgumentException("Invalid cell type for User ID in cell 0");
                    }
                } else {
                    throw new IllegalArgumentException("User ID not found in cell 0");
                }

                Cell cell2 = currentRow.getCell(2);
                if (cell2 != null && cell2.getCellType() == CellType.NUMERIC) {
                    Long kelasId = (long) cell2.getNumericCellValue();
                    Kelas kelas = kelasRepo.findById(kelasId)
                            .orElseThrow(() -> new NotFoundException("Kelas dengan ID " + kelasId + " tidak ditemukan"));
                    kbm.setKelas(kelas);
                } else {
                    throw new IllegalArgumentException("Kelas ID not found or invalid format in cell 1");
                }

                Cell cell3 = currentRow.getCell(3);
                if (cell3 != null && cell3.getCellType() == CellType.STRING) {
                    kbm.setJam_masuk(cell3.getStringCellValue());
                }

                Cell cell4 = currentRow.getCell(4);
                if (cell4 != null && cell4.getCellType() == CellType.STRING) {
                    kbm.setJam_pulang(cell4.getStringCellValue());
                }

                Cell cell5 = currentRow.getCell(5);
                if (cell5 != null && cell5.getCellType() == CellType.STRING) {
                    kbm.setMateri(cell5.getStringCellValue());
                }

                Cell cell6 = currentRow.getCell(6);
                if (cell6 != null && cell6.getCellType() == CellType.STRING) {
                    kbm.setKeterangan(cell6.getStringCellValue());
                }

                kbmRepo.save(kbm);
            }
        }
    }

    public void excelKbmTemplate(HttpServletResponse response) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Templat-KBM");

        int rowNum = 0;
        Row titleRow = sheet.createRow(rowNum++);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("TEMPLAT DATA KBM");

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));

        CellStyle titleStyle = workbook.createCellStyle();
        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 14);
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);

        titleCell.setCellStyle(titleStyle);

        Row headerRow = sheet.createRow(rowNum++);
        String[] headers = {"No", "Nama Guru", "Kelas", "Jam Masuk", "Jam Pulang", "Materi", "Keterangan"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setFillForegroundColor(IndexedColors.LIME.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        for (int i = 0; i < headers.length; i++) {
            headerRow.getCell(i).setCellStyle(headerStyle);
        }

        sheet.setColumnWidth(1, 256 * 20);
        sheet.setColumnWidth(2, 256 * 20);
        sheet.setColumnWidth(3, 256 * 20);
        sheet.setColumnWidth(4, 256 * 20);
        sheet.setColumnWidth(5, 256 * 15);
        sheet.setColumnWidth(6, 256 * 15);

        sheet.autoSizeColumn(0);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=TemplateKBM.xlsx");
        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
