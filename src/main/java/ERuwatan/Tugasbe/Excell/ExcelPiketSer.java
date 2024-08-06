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
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ExcelPiketSer {
    @Autowired
    private PiketSer piketSer;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private static final Logger LOGGER = Logger.getLogger(ExcelPiketSer.class.getName());

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

    public List<String> importPiketFromExcel(MultipartFile file) throws IOException {
        List<String> messages = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() < 2) {
                    continue; // Skip header rows
                }

                try {
                    PiketDTO piketDTO = new PiketDTO();
                    piketDTO.setKelasId(getLongCellValue(row.getCell(1)));
                    piketDTO.setTanggal(getDateCellValue(row.getCell(2)));

                    List<SiswaStatusDTO> siswaStatusList = new ArrayList<>();
                    SiswaStatusDTO siswaStatusDTO = new SiswaStatusDTO();
                    siswaStatusDTO.setSiswaId(getLongCellValue(row.getCell(3)));
                    List<String> statusList = new ArrayList<>();
                    statusList.add(getStringCellValue(row.getCell(4)));
                    siswaStatusDTO.setStatusList(statusList);
                    siswaStatusList.add(siswaStatusDTO);

                    piketDTO.setSiswaStatusList(siswaStatusList);

                    piketSer.createPiket(piketDTO);
                } catch (EntityNotFoundException e) {
                    String errorMessage = "Siswa dengan ID " + getLongCellValue(row.getCell(3)) + " tidak ditemukan pada baris " + row.getRowNum();
                    messages.add(errorMessage);
                    LOGGER.log(Level.WARNING, errorMessage, e);
                } catch (IllegalArgumentException e) {
                    String errorMessage = "Data tidak valid pada baris " + row.getRowNum() + ": " + e.getMessage();
                    messages.add(errorMessage);
                    LOGGER.log(Level.WARNING, errorMessage, e);
                }
            }
        }

        return messages;
    }

    private Long getLongCellValue(Cell cell) {
        if (cell.getCellType() == CellType.NUMERIC) {
            return (long) cell.getNumericCellValue();
        } else if (cell.getCellType() == CellType.STRING) {
            try {
                return Long.parseLong(cell.getStringCellValue());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid long format: " + cell.getStringCellValue(), e);
            }
        } else {
            throw new IllegalArgumentException("Unexpected cell type for long value: " + cell.getCellType());
        }
    }

    private String getStringCellValue(Cell cell) {
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf(cell.getNumericCellValue());
        } else {
            throw new IllegalArgumentException("Unexpected cell type for string value: " + cell.getCellType());
        }
    }

    private java.util.Date getDateCellValue(Cell cell) {
        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            return cell.getDateCellValue();
        } else if (cell.getCellType() == CellType.STRING) {
            try {
                return dateFormat.parse(cell.getStringCellValue());
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid date format: " + cell.getStringCellValue(), e);
            }
        } else {
            throw new IllegalArgumentException("Unexpected cell type for date value: " + cell.getCellType());
        }
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
