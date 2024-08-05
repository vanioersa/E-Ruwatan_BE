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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;

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

    public void importPiketDataFromExcel(String filePath) throws IOException {
        FileInputStream file = new FileInputStream(new File(filePath));
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            // Skip the first two rows
            if (row.getRowNum() < 2) {
                continue;
            }

            // Iterate through each cell in the row
            for (Cell cell : row) {
                switch (cell.getCellType()) {
                    case STRING:
                        String stringValue = cell.getStringCellValue();
                        System.out.println("String value: " + stringValue);
                        // Process string value
                        break;
                    case NUMERIC:
                        if (DateUtil.isCellDateFormatted(cell)) {
                            Date dateValue = cell.getDateCellValue();
                            System.out.println("Date value: " + dateValue);
                            // Process date value
                        } else {
                            double numericValue = cell.getNumericCellValue();
                            System.out.println("Numeric value: " + numericValue);
                            // Process numeric value
                        }
                        break;
                    case BOOLEAN:
                        boolean booleanValue = cell.getBooleanCellValue();
                        System.out.println("Boolean value: " + booleanValue);
                        // Process boolean value
                        break;
                    case FORMULA:
                        // Evaluate the formula and process the resulting value
                        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                        CellValue cellValue = evaluator.evaluate(cell);
                        switch (cellValue.getCellType()) {
                            case STRING:
                                String formulaStringValue = cellValue.getStringValue();
                                System.out.println("Formula string value: " + formulaStringValue);
                                // Process formula string value
                                break;
                            case NUMERIC:
                                double formulaNumericValue = cellValue.getNumberValue();
                                System.out.println("Formula numeric value: " + formulaNumericValue);
                                // Process formula numeric value
                                break;
                            case BOOLEAN:
                                boolean formulaBooleanValue = cellValue.getBooleanValue();
                                System.out.println("Formula boolean value: " + formulaBooleanValue);
                                // Process formula boolean value
                                break;
                        }
                        break;
                    case BLANK:
                        System.out.println("Blank cell");
                        // Handle blank cell
                        break;
                    default:
                        System.out.println("Unknown cell type");
                        // Handle unknown cell type
                        break;
                }
            }
        }

        workbook.close();
        file.close();
    }

//    public void importPiketDataFromExcel(MultipartFile file) throws IOException {
//        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
//            Sheet sheet = workbook.getSheetAt(0);
//            int rowNum = 0;
//            for (Row row : sheet) {
//                if (rowNum++ == 0) continue; // Skip header row
//
//                PiketDTO piketDTO = new PiketDTO();
//                // Skip cell 0 as it is for "No"
//                piketDTO.setKelasId((long) row.getCell(1).getNumericCellValue());
//                piketDTO.setTanggal(row.getCell(2).getDateCellValue());
//
//                List<SiswaStatusDTO> siswaStatusList = extractSiswaStatusList(row);
//                piketDTO.setSiswaStatusList(siswaStatusList);
//
//                piketSer.createPiket(piketDTO);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new IOException("Error processing Excel file: " + e.getMessage());
//        }
//    }

//    private List<SiswaStatusDTO> extractSiswaStatusList(Row row) {
//        List<SiswaStatusDTO> siswaStatusList = new ArrayList<>();
//        int numberOfCells = row.getLastCellNum();
//
//        for (int i = 3; i < numberOfCells; i++) { // Start from cell 3 to skip cells 0, 1, and 2
//            Cell cell = row.getCell(i);
//            if (cell != null) {
//                SiswaStatusDTO siswaStatusDTO = new SiswaStatusDTO();
//                siswaStatusDTO.setSiswaId((long) i); // Adjust this to properly map student ID if needed
//                siswaStatusDTO.setStatusList(Collections.singletonList(cell.getStringCellValue()));
//                siswaStatusList.add(siswaStatusDTO);
//            }
//        }
//
//        return siswaStatusList;
//    }
}
