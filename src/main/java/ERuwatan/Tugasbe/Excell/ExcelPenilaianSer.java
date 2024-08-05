package ERuwatan.Tugasbe.Excell;

import ERuwatan.Tugasbe.model.*;
import ERuwatan.Tugasbe.repository.KelasRepo;
import ERuwatan.Tugasbe.repository.PenilaianRepo;
import ERuwatan.Tugasbe.repository.SiswaRepo;
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

@Service
public class ExcelPenilaianSer {
    @Autowired
    private PenilaianRepo penilaianRepo;

    @Autowired
    private KelasRepo kelasRepo;

    @Autowired
    private SiswaRepo siswaRepo;

    public void excelExportPenilaian(HttpServletResponse response) throws IOException, NotFoundException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Export-Penilaian");

        int rowNum = 0;
        Row titleRow = sheet.createRow(rowNum++);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("DATA PENILAIAN");

        CellStyle titleStyle = workbook.createCellStyle();
        Font titleFont = workbook.createFont();
        titleFont.setFontHeightInPoints((short) 16);
        titleFont.setBold(true);
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        titleCell.setCellStyle(titleStyle);

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));

        Row headerRow = sheet.createRow(rowNum++);
        String[] headers = {"No", "Nama Siswa", "Kelas", "Nilai Siswa", "Deskripsi"};

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

        List<Penilaian> penilaianList = penilaianRepo.findAll();
        penilaianList.sort((u1, u2) -> Long.compare(u2.getId(), u1.getId()));

        int no = 1;
        for (Penilaian penilaian : penilaianList) {
            Row row = sheet.createRow(rowNum++);
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(no++);

            Cell cell1 = row.createCell(1);
            cell1.setCellValue(penilaian.getSiswa().getNama_siswa());

            Cell cell2 = row.createCell(2);
            String kelas = penilaian.getKelas().getKelas();
            String nama_kelas = penilaian.getKelas().getNama_kelas();
            cell2.setCellValue(kelas + " - " + nama_kelas);

            Cell cell3 = row.createCell(3);
            cell3.setCellValue(penilaian.getNilai());

            Cell cell4 = row.createCell(4);
            cell4.setCellValue(penilaian.getDeskripsi());
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=ExportPenilaian.xlsx");
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    public void importPenilaianFromExcel(MultipartFile file) throws IOException {
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
                Penilaian penilaian = new Penilaian();

                Cell cell1 = currentRow.getCell(1);
                if (cell1 != null) {
                    if (cell1.getCellType() == CellType.NUMERIC) {
                        Siswa siswa = siswaRepo.findById((long) cell1.getNumericCellValue())
                                .orElseThrow(() -> new ERuwatan.Tugasbe.exception.NotFoundException("Id " + cell1.getNumericCellValue() + " not found"));
                        penilaian.setSiswa(siswa);
                    } else if (cell1.getCellType() == CellType.STRING) {
                        try {
                            Long kelasId = Long.valueOf(cell1.getStringCellValue());
                            Siswa kelas = siswaRepo.findById(kelasId)
                                    .orElseThrow(() -> new ERuwatan.Tugasbe.exception.NotFoundException("Id " + kelasId + " not found"));
                            penilaian.setSiswa(kelas);
                        } catch (NumberFormatException e) {
                            throw new IllegalArgumentException("Invalid Kelas ID: " + cell1.getStringCellValue());
                        }
                    }
                }

                Cell cell2 = currentRow.getCell(2);
                if (cell2 != null) {
                    if (cell2.getCellType() == CellType.NUMERIC) {
                        Kelas kelas = kelasRepo.findById((long) cell2.getNumericCellValue())
                                .orElseThrow(() -> new NotFoundException("Id " + cell2.getNumericCellValue() + " not found"));
                        penilaian.setKelas(kelas);
                    } else if (cell2.getCellType() == CellType.STRING) {
                        try {
                            Long kelasId = Long.valueOf(cell2.getStringCellValue());
                            Kelas kelas = kelasRepo.findById(kelasId)
                                    .orElseThrow(() -> new NotFoundException("Id " + kelasId + " not found"));
                            penilaian.setKelas(kelas);
                        } catch (NumberFormatException e) {
                            throw new IllegalArgumentException("Invalid Kelas ID: " + cell2.getStringCellValue());
                        }
                    }
                }

                Cell cell3 = currentRow.getCell(3);
                if (cell3 != null && cell3.getCellType() == CellType.NUMERIC) {
                    penilaian.setNilai(String.valueOf((int) cell3.getNumericCellValue()));
                }

                Cell cell4 = currentRow.getCell(4);
                if (cell4 != null && cell4.getCellType() == CellType.STRING) {
                    penilaian.setDeskripsi(cell4.getStringCellValue());
                }

                penilaianRepo.save(penilaian);
            }
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void excelDownloadPenilaianTemplate(HttpServletResponse response) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Templat-Penilaian");

        int rowNum = 0;
        Row titleRow = sheet.createRow(rowNum++);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("TEMPLAT DATA PENILAIAN");

        CellStyle titleStyle = workbook.createCellStyle();
        Font titleFont = workbook.createFont();
        titleFont.setFontHeightInPoints((short) 16);
        titleFont.setBold(true);
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        titleCell.setCellStyle(titleStyle);

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));

        Row headerRow = sheet.createRow(rowNum++);
        String[] headers = {"No", "Nama Siswa", "Kelas", "Nilai Siswa", "Deskripsi"};

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

        sheet.setColumnWidth(0, 256 * 5);
        sheet.setColumnWidth(1, 256 * 20);
        sheet.setColumnWidth(2, 256 * 15);
        sheet.setColumnWidth(3, 256 * 15);
        sheet.setColumnWidth(4, 256 * 20);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=TemplatePenilaian.xlsx");
        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
