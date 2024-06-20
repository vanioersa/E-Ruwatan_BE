//package ERuwatan.Tugasbe.Excell;
//
//import ERuwatan.Tugasbe.model.Piket;
//import ERuwatan.Tugasbe.repository.PiketRepo;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.ss.util.CellRangeAddress;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.servlet.http.HttpServletResponse;
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.List;
//
//@Service
//public class ExcelPiketSer {
//    @Autowired
//    private PiketRepo piketRepo;
//
//    public ByteArrayInputStream loadPiket() throws IOException {
//        List<Piket> pikets = piketRepo.findAll();
//        ByteArrayInputStream in = ExcelPiket.piketToExcel(pikets);
//        return in;
//    }
//
//    public void savePiket(MultipartFile file) {
//        try {
//            List<Piket> piketList = ExcelPiket.excelPiket(file.getInputStream());
//            piketRepo.saveAll(piketList);
//        } catch (IOException e) {
//            throw new RuntimeException("fail to store excel data: " + e.getMessage());
//        }
//    }
//
//    public void excelExportPiket(String tanggal, Long kelas_id, HttpServletResponse response) throws IOException {
//        Workbook workbook = new XSSFWorkbook();
//        Sheet sheet = workbook.createSheet("Export-Piket");
//
//        List<Piket> absensiList = piketRepo.findByTanggalAndKelasId(tanggal, kelas_id);
//
//        int rowNum = 0;
//
//        // Header row
//        Row headerRow = sheet.createRow(rowNum++);
//        String[] headers = {"ID", "Status", "Tanggal", "Kelas"};
//        for (int i = 0; i < headers.length; i++) {
//            Cell cell = headerRow.createCell(i);
//            cell.setCellValue(headers[i]);
//        }
//
//        // Data rows
//        for (Piket piket : absensiList) {
//            Row row = sheet.createRow(rowNum++);
//            Cell cell0 = row.createCell(0);
//            cell0.setCellValue(piket.getId());
//
//            Cell cell1 = row.createCell(1);
//            cell1.setCellValue(piket.getTanggal());
//
//            Cell cell2 = row.createCell(2);
//            cell2.setCellValue(piket.getStatus());
//
//            Cell cell3 = row.createCell(3);
//            cell3.setCellValue(piket.getKelas().getNama_kelas()); // Assuming Kelas ID is stored in a field called "kelas"
//
////            Cell cell4 = row.createCell(4);
////            cell4.setCellValue(piket.getSiswa().getNama_siswa()); // Assuming Siswa ID is stored in a field called "siswa"
//        }
//
//        // Adjust column width
//        for (int i = 0; i < headers.length; i++) {
//            sheet.autoSizeColumn(i);
//        }
//
//        // Set response headers for Excel download
//        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//        response.setHeader("Content-Disposition", "attachment; filename=ExportPiket.xlsx");
//        workbook.write(response.getOutputStream());
//        workbook.close();
//    }
//
//}
