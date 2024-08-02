package ERuwatan.Tugasbe.Excell;

import ERuwatan.Tugasbe.model.Kelas;
import ERuwatan.Tugasbe.repository.KelasRepo;
import ERuwatan.Tugasbe.repository.UserRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Service
public class ExcelKelasSer {

    @Autowired
    private KelasRepo kelasRepo;

    @Autowired
    private UserRepository userRepository;

    public void excelExportKelas(HttpServletResponse response) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Ekspor-Kelas");

        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("DATA KELAS");

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));

        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        Font titleFont = workbook.createFont();
        titleFont.setFontHeightInPoints((short) 16);
        titleFont.setBold(true);
        titleStyle.setFont(titleFont);
        titleCell.setCellStyle(titleStyle);

        int rowNum = 1;

        Row headerRow = sheet.createRow(rowNum++);
        String[] headers = {"No", "Kelas", "Nama Kelas"};

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setFillForegroundColor(IndexedColors.LIME.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(headerFont);

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        List<Kelas> kelasList = kelasRepo.findAll();
        kelasList.sort((u1, u2) -> Long.compare(u2.getId(), u1.getId()));

        int no = 1;
        for (Kelas kelas : kelasList) {
            Row row = sheet.createRow(rowNum++);
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(no++);

            Cell cell1 = row.createCell(1);
            cell1.setCellValue(kelas.getKelas());

            Cell cell2 = row.createCell(2);
            cell2.setCellValue(kelas.getNama_kelas());
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=ExportKelas.xlsx");
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    public void importKelasFromExcel(MultipartFile file) throws IOException {
        Set<String> existingKelasSet = new HashSet<>();
        List<Kelas> existingKelasList = kelasRepo.findAll();
        for (Kelas kelas : existingKelasList) {
            existingKelasSet.add(kelas.getNama_kelas() + "|" + kelas.getKelas());
        }

        StringBuilder errorMessages = new StringBuilder();
        boolean hasErrors = false;

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
                Kelas kelas = new Kelas();
                String kelasValue = getCellValueAsString(currentRow.getCell(1));
                String namaKelas = getCellValueAsString(currentRow.getCell(2));

                if (kelasValue == null || namaKelas == null) {
                    errorMessages.append("Data pada baris ").append(currentRow.getRowNum() + 1).append(" tidak lengkap.\n");
                    hasErrors = true;
                    continue;
                }

                if (existingKelasSet.contains(namaKelas + "|" + kelasValue)) {
                    errorMessages.append("Kelas '").append(kelasValue)
                            .append("' Nama Kelas '").append(namaKelas)
                            .append("' sudah digunakan.\n");
                    hasErrors = true;
                    continue;
                }

                kelas.setKelas(kelasValue);
                kelas.setNama_kelas(namaKelas);
                kelasRepo.save(kelas);
                existingKelasSet.add(namaKelas + "|" + kelasValue);
            }
        } catch (Exception e) {
            throw new RuntimeException("Gagal mengimpor data dari file Excel", e);
        }

        if (hasErrors) {
            throw new IOException(errorMessages.toString());
        }
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
            default:
                return null;
        }
    }

    public void downloadKelasTemplate(HttpServletResponse response) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Templat-Kelas");

        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("TEMPLAT DATA KELAS");

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));

        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 14);
        titleStyle.setFont(titleFont);
        titleCell.setCellStyle(titleStyle);

        Row headerRow = sheet.createRow(1);
        String[] headers = {"No", "Kelas", "Nama Kelas"};
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setFillForegroundColor(IndexedColors.LIME.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(headerFont);

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        sheet.autoSizeColumn(0);

        sheet.setColumnWidth(1, 15 * 256);
        sheet.setColumnWidth(2, 18 * 256);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=KelasTemplate.xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
