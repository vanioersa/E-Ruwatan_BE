package ERuwatan.Tugasbe.Excell;

import ERuwatan.Tugasbe.exception.NotFoundException;
import ERuwatan.Tugasbe.model.Kelas;
import ERuwatan.Tugasbe.model.Siswa;
import ERuwatan.Tugasbe.repository.KelasRepo;
import ERuwatan.Tugasbe.repository.SiswaRepo;
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

@Service
public class ExcelSiswaSer {

    @Autowired
    private SiswaRepo siswaRepo;

    @Autowired
    private KelasRepo kelasRepo;

    public void excelExportSiswa(HttpServletResponse response) throws IOException{
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Ekspor-Siswa");

        int rowNum = 0;
        Row titleRow = sheet.createRow(rowNum++);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("DATA SISWA");

        CellStyle titleStyle = workbook.createCellStyle();
        Font titleFont = workbook.createFont();
        titleFont.setFontHeightInPoints((short) 16);
        titleFont.setBold(true);
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        titleCell.setCellStyle(titleStyle);

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 10));

        Row headerRow = sheet.createRow(rowNum++);
        String[] headers = {"No", "Nama Lengkap", "Gender", "Tanggal Lahir", "Tempat Lahir", "Alamat Rumah", "Nomor Telepon", "NIK", "NIS", "NISN", "Kelas"};

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

        List<Siswa> siswaList = siswaRepo.findAll();
        siswaList.sort((u1, u2) -> Long.compare(u2.getId(), u1.getId()));

        int index = 1;
        for (Siswa siswa : siswaList) {
            Row row = sheet.createRow(rowNum++);
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(index++);

            Cell cell1 = row.createCell(1);
            cell1.setCellValue(siswa.getNama_siswa());

            Cell cell2 = row.createCell(2);
            cell2.setCellValue(siswa.getGender());

            Cell cell3 = row.createCell(3);
            cell3.setCellValue(siswa.getTanggal());

            Cell cell4 = row.createCell(4);
            cell4.setCellValue(siswa.getTempat());

            Cell cell5 = row.createCell(5);
            cell5.setCellValue(siswa.getAlamat());

            Cell cell6 = row.createCell(6);
            cell6.setCellValue(siswa.getTelepon());

            Cell cell7 = row.createCell(7);
            cell7.setCellValue(siswa.getNik());

            Cell cell8 = row.createCell(8);
            cell8.setCellValue(siswa.getNis());

            Cell cell9 = row.createCell(9);
            cell9.setCellValue(siswa.getNisn());

            Cell cell10 = row.createCell(10);
            String kelas = siswa.getKelas().getKelas();
            String nama_kelas = siswa.getKelas().getNama_kelas();
            cell10.setCellValue(kelas + " - " + nama_kelas);
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=ExportSiswa.xlsx");
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    public void importSiswaFromExcel(MultipartFile file) throws IOException {
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
                Siswa siswa = new Siswa();

                Cell cell1 = currentRow.getCell(1);
                if (cell1 != null && cell1.getCellType() == CellType.STRING) {
                    siswa.setNama_siswa(cell1.getStringCellValue());
                }

                Cell cell2 = currentRow.getCell(2);
                if (cell2 != null && cell2.getCellType() == CellType.STRING) {
                    siswa.setGender(cell2.getStringCellValue());
                }

                Cell cell3 = currentRow.getCell(3);
                if (cell3 != null && cell3.getCellType() == CellType.STRING) {
                    siswa.setTanggal(cell3.getStringCellValue());
                }

                Cell cell4 = currentRow.getCell(4);
                if (cell4 != null && cell4.getCellType() == CellType.STRING) {
                    siswa.setTempat(cell4.getStringCellValue());
                }

                Cell cell5 = currentRow.getCell(5);
                if (cell5 != null && cell5.getCellType() == CellType.STRING) {
                    siswa.setAlamat(cell5.getStringCellValue());
                }

                Cell cell6 = currentRow.getCell(6);
                if (cell6 != null && cell6.getCellType() == CellType.STRING) {
                    siswa.setTelepon(cell6.getStringCellValue());
                }

                Cell cell7 = currentRow.getCell(7);
                if (cell7 != null && cell7.getCellType() == CellType.STRING) {
                    siswa.setNik(cell7.getStringCellValue());
                }

                Cell cell8 = currentRow.getCell(8);
                if (cell8 != null && cell8.getCellType() == CellType.STRING) {
                    siswa.setNis(cell8.getStringCellValue());
                }

                Cell cell9 = currentRow.getCell(9);
                if (cell9 != null && cell9.getCellType() == CellType.STRING) {
                    siswa.setNisn(cell9.getStringCellValue());
                }

                Cell cell10 = currentRow.getCell(10);
                if (cell10 != null) {
                    if (cell10.getCellType() == CellType.STRING) {
                        try {
                            Long kelasId = Long.valueOf(cell10.getStringCellValue());
                            Kelas kelas = kelasRepo.findById(kelasId)
                                    .orElseThrow(() -> new NotFoundException("Id " + kelasId + " not found"));
                            siswa.setKelas(kelas);
                        } catch (NumberFormatException e) {
                            throw new IllegalArgumentException("Invalid Kelas ID: " + cell10.getStringCellValue());
                        }
                    } else if (cell10.getCellType() == CellType.NUMERIC) {
                        Kelas kelas = kelasRepo.findById((long) cell10.getNumericCellValue())
                                .orElseThrow(() -> new NotFoundException("Id " + cell10.getNumericCellValue() + " not found"));
                        siswa.setKelas(kelas);
                    }
                }

                siswaRepo.save(siswa);
            }
        }
    }


    public void excelSiswaTemplate(HttpServletResponse response) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Templat-Siswa");

        int rowNum = 0;

        Row titleRow = sheet.createRow(rowNum++);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("TEMPLAT DATA SISWA");

        CellStyle titleStyle = workbook.createCellStyle();
        Font titleFont = workbook.createFont();
        titleFont.setFontHeightInPoints((short) 16);
        titleFont.setBold(true);
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        titleCell.setCellStyle(titleStyle);

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 10));

        Row headerRow = sheet.createRow(rowNum++);
        String[] headers = {"No.", "Nama Lengkap", "Gender", "Tanggal Lahir", "Tempat Lahir", "Alamat Rumah", "Nomor Telepon", "NIK", "NIS", "NISN", "Kelas"};

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

        sheet.setColumnWidth(0, 10 * 256);
        sheet.setColumnWidth(1, 20 * 256);
        sheet.setColumnWidth(2, 15 * 256);
        sheet.setColumnWidth(3, 20 * 256);
        sheet.setColumnWidth(4, 20 * 256);
        sheet.setColumnWidth(5, 20 * 256);
        sheet.setColumnWidth(6, 20 * 256);
        sheet.setColumnWidth(7, 15 * 256);
        sheet.setColumnWidth(8, 15 * 256);
        sheet.setColumnWidth(9, 15 * 256);
        sheet.setColumnWidth(10, 15 * 256);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=TemplateSiswa.xlsx");
        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
