package ERuwatan.Tugasbe.Excell;

import ERuwatan.Tugasbe.dto.KelasDTO;
import ERuwatan.Tugasbe.exception.NotFoundException;
import ERuwatan.Tugasbe.model.Kbm;
import ERuwatan.Tugasbe.model.Kelas;
import ERuwatan.Tugasbe.model.Siswa;
import ERuwatan.Tugasbe.repository.KelasRepo;
import ERuwatan.Tugasbe.repository.SiswaRepo;
import org.apache.poi.ss.usermodel.*;
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
public class ExcelSiswaSer {

    @Autowired
    private SiswaRepo siswaRepo;

    @Autowired
    private KelasRepo kelasRepo;

    public void excelExportSiswa(Long kelas_id, HttpServletResponse response) throws IOException{
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Export-Siswa");

        List<Siswa> siswaList = siswaRepo.findByKelasId(kelas_id);

        int rowNum = 0;

        Row headerRow = sheet.createRow(rowNum++);
        String[] headers = {"ID", "Nama Siswa", "NISN", "Tempat Lahir", "Kelas", "Alamat"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        for (Siswa siswa : siswaList) {
            Row row = sheet.createRow(rowNum++);
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(siswa.getId());

            Cell cell1 = row.createCell(1);
            cell1.setCellValue(siswa.getNama_siswa());

            Cell cell2 = row.createCell(2);
            cell2.setCellValue(siswa.getNisn());

            Cell cell3 = row.createCell(3);
            cell3.setCellValue(siswa.getTempat());

            Cell cell4 = row.createCell(4);
            String kelas = siswa.getKelas().getKelas();
            String nama_kelas = siswa.getKelas().getNama_kelas();
            cell4.setCellValue(nama_kelas);

            Cell cell5 = row.createCell(5);
            cell5.setCellValue(siswa.getAlamat());

        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=ExportSiswa.xlsx");
        workbook.write(response.getOutputStream());
        workbook.close();
    }
//    aaaaaaa

    public void importSiswaFromExcel(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            // Skip header row
            if (rows.hasNext()) {
                rows.next();
            }

            while (rows.hasNext()) {
                Row currentRow = rows.next();
                Siswa siswa = new Siswa();

                // Handle Nama Siswa
                Cell cell1 = currentRow.getCell(1);
                if (cell1 != null && cell1.getCellType() == CellType.STRING) {
                    siswa.setNama_siswa(cell1.getStringCellValue());
                }

                // Handle NISN
                Cell cell2 = currentRow.getCell(2);
                if (cell2 != null && cell2.getCellType() == CellType.STRING) {
                    siswa.setNisn(cell2.getStringCellValue());
                }

                // Handle Tempat Lahir
                Cell cell3 = currentRow.getCell(3);
                if (cell3 != null && cell3.getCellType() == CellType.STRING) {
                    siswa.setTempat(cell3.getStringCellValue());
                }

                // Handle Kelas (ID)
                Cell cell4 = currentRow.getCell(4);
                if (cell4 != null) {
                    if (cell4.getCellType() == CellType.NUMERIC) {
                        Kelas kelas = kelasRepo.findById((long) cell4.getNumericCellValue())
                                .orElseThrow(() -> new NotFoundException("Id " + cell4.getNumericCellValue() + " not found"));
                        siswa.setKelas(kelas);
                    } else if (cell4.getCellType() == CellType.STRING) {
                        try {
                            Long kelasId = Long.valueOf(cell4.getStringCellValue());
                            Kelas kelas = kelasRepo.findById(kelasId)
                                    .orElseThrow(() -> new NotFoundException("Id " + kelasId + " not found"));
                            siswa.setKelas(kelas);
                        } catch (NumberFormatException e) {
                            throw new IllegalArgumentException("Invalid Kelas ID: " + cell4.getStringCellValue());
                        }
                    }
                }

                // Handle Alamat
                Cell cell5 = currentRow.getCell(5);
                if (cell5 != null && cell5.getCellType() == CellType.STRING) {
                    siswa.setAlamat(cell5.getStringCellValue());
                }

                // Save the Siswa entity to the repository
                siswaRepo.save(siswa);
            }
        }
    }

    public void excelSiswaTemplate(HttpServletResponse response) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Siswa Template");

        int rowNum = 0;

        Row headerRow = sheet.createRow(rowNum++);
        String[] headers = {"No", "Nama Siswa", "NISN", "Tempat Lahir", "Kelas", "Alamat"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=TemplateSiswa.xlsx");
        workbook.write(response.getOutputStream());
        workbook.close();
    }


}
