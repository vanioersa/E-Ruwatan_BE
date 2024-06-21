package ERuwatan.Tugasbe.Excell;

import ERuwatan.Tugasbe.dto.KelasDTO;
import ERuwatan.Tugasbe.dto.PiketDTO;
import ERuwatan.Tugasbe.dto.SiswaDTO;
import ERuwatan.Tugasbe.dto.SiswaStatusDTO;
import ERuwatan.Tugasbe.service.PiketSer;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
            Sheet sheet = workbook.createSheet("Piket Data");

            List<PiketDTO> piketList = piketSer.getAllPiket();

            int rowNum = 0;
            Row headerRow = sheet.createRow(rowNum++);
            headerRow.createCell(0).setCellValue("No");
            headerRow.createCell(1).setCellValue("Kelas");
            headerRow.createCell(2).setCellValue("Tanggal");
            headerRow.createCell(3).setCellValue("Nama Siswa");
            headerRow.createCell(4).setCellValue("Status");

            for (PiketDTO piket : piketList) {
                for (SiswaStatusDTO siswaStatus : piket.getSiswaStatusList()) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(rowNum - 1 + ".");
                    KelasDTO kelasDTO = piketSer.getKelasById(piket.getKelasId());
                    String kelasNamaKelas = kelasDTO.getKelas() + " - " + kelasDTO.getNama_kelas();
                    row.createCell(1).setCellValue(kelasNamaKelas);
                    row.createCell(2).setCellValue(dateFormat.format(piket.getTanggal()));
                    SiswaDTO siswaDTO = piketSer.getSiswaById(siswaStatus.getSiswaId());
                    row.createCell(3).setCellValue(siswaDTO.getNama_siswa());
                    row.createCell(4).setCellValue(String.join(", ", siswaStatus.getStatusList()));
                }
            }

            // Auto size columns
            for (int i = 0; i < 5; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("attachment", "piket_data.xlsx");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(outputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
