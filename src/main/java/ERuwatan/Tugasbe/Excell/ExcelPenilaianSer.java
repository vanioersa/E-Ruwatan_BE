package ERuwatan.Tugasbe.Excell;

import ERuwatan.Tugasbe.model.*;
import ERuwatan.Tugasbe.repository.KelasRepo;
import ERuwatan.Tugasbe.repository.PenilaianRepo;
import ERuwatan.Tugasbe.repository.SiswaRepo;
import javassist.NotFoundException;
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

@Service
public class ExcelPenilaianSer {
    @Autowired
    private PenilaianRepo penilaianRepo;

    @Autowired
    private KelasRepo kelasRepo;

    @Autowired
    private SiswaRepo siswaRepo;

    public void excelExportPenilaian( HttpServletResponse response) throws IOException, NotFoundException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Export-Penilaian");

        List<Penilaian> penilaianList = penilaianRepo.findAll();

        int rowNum = 0;

        Row headerRow = sheet.createRow(rowNum++);
        String[] headers = {"ID", "Nama Siswa", "Kelas ID", "Nilai Siswa", "Deskripsi"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        for (Penilaian penilaian : penilaianList) {
            Row row = sheet.createRow(rowNum++);
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(penilaian.getId());

            Cell cell1 = row.createCell(1);
            cell1.setCellValue(penilaian.getSiswa().getNama_siswa());

            Cell cell2 = row.createCell(2);
            cell2.setCellValue(penilaian.getKelas().getId());

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

            // Skip header row
            if (rows.hasNext()) {
                rows.next();
            }

            while (rows.hasNext()) {
                Row currentRow = rows.next();
                Penilaian penilaian = new Penilaian();

                // Handle Nama Siswa
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

                // Handle Kelas by ID
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

                // Handle Nilai
                Cell cell3 = currentRow.getCell(3);
                if (cell3 != null && cell3.getCellType() == CellType.NUMERIC) {
                    penilaian.setNilai(String.valueOf((int) cell3.getNumericCellValue()));
                }

                // Handle Deskripsi
                Cell cell4 = currentRow.getCell(4);
                if (cell4 != null && cell4.getCellType() == CellType.STRING) {
                    penilaian.setDeskripsi(cell4.getStringCellValue());
                }

                // Save the Penilaian entity to the repository
                penilaianRepo.save(penilaian);
            }
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
