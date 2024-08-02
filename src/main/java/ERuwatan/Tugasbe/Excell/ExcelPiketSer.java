package ERuwatan.Tugasbe.Excell;

import ERuwatan.Tugasbe.dto.KelasDTO;
import ERuwatan.Tugasbe.dto.PiketDTO;
import ERuwatan.Tugasbe.dto.SiswaStatusDTO;
import ERuwatan.Tugasbe.service.PiketSer;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class ExcelPiketSer {
    @Autowired
    private PiketSer piketSer;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public ResponseEntity<byte[]> exportPiketDataToExcel() {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Ekspor-Piketan");

            List<PiketDTO> piketList = piketSer.getAllPiket();

            int rowNum = 0;
            Row headerRow = sheet.createRow(rowNum++);
            Cell piketanCell = headerRow.createCell(0);
            piketanCell.setCellValue("DATA PIKETAN");
            CellStyle piketanStyle = workbook.createCellStyle();
            piketanStyle.setAlignment(HorizontalAlignment.CENTER);
            piketanStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            piketanStyle.setFont(createBoldFont(workbook));
            piketanCell.setCellStyle(piketanStyle);

            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));

            Row actualHeaderRow = sheet.createRow(rowNum++);
            String[] headers = {"No", "Kelas", "Tanggal", "Nama Siswa", "Status"};
            CellStyle headerStyle = createHeaderStyle(workbook);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = actualHeaderRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            CellStyle centerStyle = workbook.createCellStyle();
            centerStyle.setAlignment(HorizontalAlignment.CENTER);
            centerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            CellStyle blueStyle = createCustomStyle(workbook, "#003C8C");
            CellStyle yellowStyle = createCustomStyle(workbook, "#F57F17");
            CellStyle greenStyle = createCustomStyle(workbook, "#2C6B2F");
            CellStyle redStyle = createCustomStyle(workbook, "#C62828");

            int no = 1;
            for (PiketDTO piket : piketList) {
                KelasDTO kelasDTO = piketSer.getKelasById(piket.getKelasId());
                String nomor = Integer.toString(no++);
                String kelasNamaKelas = kelasDTO.getKelas() + " - " + kelasDTO.getNama_kelas();
                String tanggal = dateFormat.format(piket.getTanggal());

                int startRow = rowNum;
                boolean isFirstRow = true;

                for (SiswaStatusDTO siswaStatus : piket.getSiswaStatusList()) {
                    Row row = sheet.createRow(rowNum++);
                    if (isFirstRow) {
                        Cell nomorCell = row.createCell(0);
                        nomorCell.setCellValue(nomor);
                        nomorCell.setCellStyle(centerStyle);

                        Cell kelasCell = row.createCell(1);
                        kelasCell.setCellValue(kelasNamaKelas);
                        kelasCell.setCellStyle(centerStyle);

                        Cell tanggalCell = row.createCell(2);
                        tanggalCell.setCellValue(tanggal);
                        tanggalCell.setCellStyle(centerStyle);

                        isFirstRow = false;
                    } else {
                        row.createCell(0);
                        row.createCell(1);
                        row.createCell(2);
                    }

                    row.createCell(3).setCellValue(piketSer.getSiswaById(siswaStatus.getSiswaId()).getNama_siswa());

                    Cell statusCell = row.createCell(4);
                    String status = String.join(", ", siswaStatus.getStatusList());
                    statusCell.setCellValue(status);

                    switch (status) {
                        case "Masuk":
                            statusCell.setCellStyle(blueStyle);
                            break;
                        case "Izin":
                            statusCell.setCellStyle(yellowStyle);
                            break;
                        case "Sakit":
                            statusCell.setCellStyle(greenStyle);
                            break;
                        case "Alpha":
                            statusCell.setCellStyle(redStyle);
                            break;
                        default:
                            statusCell.setCellStyle(centerStyle);
                            break;
                    }
                }

                if (rowNum > startRow + 1) {
                    sheet.addMergedRegion(new CellRangeAddress(startRow, rowNum - 1, 0, 0));
                    sheet.addMergedRegion(new CellRangeAddress(startRow, rowNum - 1, 1, 1));
                    sheet.addMergedRegion(new CellRangeAddress(startRow, rowNum - 1, 2, 2));
                }
            }

            int cmToExcelUnits = (int) (16.67 * 256);
            for (int i = 0; i < headers.length; i++) {
                if (i == 1 || i == 2) {
                    sheet.setColumnWidth(i, cmToExcelUnits);
                } else {
                    sheet.autoSizeColumn(i);
                }
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            httpHeaders.setContentDispositionFormData("attachment", "piket_data.xlsx");

            return ResponseEntity.ok()
                    .headers(httpHeaders)
                    .body(outputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    private Font createBoldFont(Workbook workbook) {
        Font font = workbook.createFont();
        font.setBold(true);
        return font;
    }

    private CellStyle createCustomStyle(Workbook workbook, String hexColor) {
        XSSFWorkbook xssfWorkbook = (XSSFWorkbook) workbook;
        XSSFCellStyle style = xssfWorkbook.createCellStyle();

        XSSFColor color = new XSSFColor(java.awt.Color.decode(hexColor), null);
        style.setFillForegroundColor(color);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Font font = xssfWorkbook.createFont();
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);

        return style;
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        XSSFWorkbook xssfWorkbook = (XSSFWorkbook) workbook;
        XSSFCellStyle style = xssfWorkbook.createCellStyle();

        Font font = xssfWorkbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);

        style.setFillForegroundColor(IndexedColors.LIME.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;
    }

    public ResponseEntity<byte[]> downloadPiketTemplate() {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Templat-Piketan");

            int rowNum = 0;
            Row headerRow = sheet.createRow(rowNum++);
            Cell piketanCell = headerRow.createCell(0);
            piketanCell.setCellValue("TEMPLAT DATA PIKETAN");
            CellStyle piketanStyle = workbook.createCellStyle();
            piketanStyle.setAlignment(HorizontalAlignment.CENTER);
            piketanStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            piketanStyle.setFont(createBoldFont(workbook));
            piketanCell.setCellStyle(piketanStyle);

            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));

            Row actualHeaderRow = sheet.createRow(rowNum++);
            String[] headers = {"No", "Kelas", "Tanggal", "Nama Siswa", "Status"};
            CellStyle headerStyle = createHeaderStyle(workbook);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = actualHeaderRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            int cmToExcelUnits = (int) (16.67 * 256);
            for (int i = 0; i < headers.length; i++) {
                if (i == 1 || i == 2) {
                    sheet.setColumnWidth(i, cmToExcelUnits);
                } else {
                    sheet.autoSizeColumn(i);
                }
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            httpHeaders.setContentDispositionFormData("attachment", "template_piket.xlsx");

            return ResponseEntity.ok()
                    .headers(httpHeaders)
                    .body(outputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
