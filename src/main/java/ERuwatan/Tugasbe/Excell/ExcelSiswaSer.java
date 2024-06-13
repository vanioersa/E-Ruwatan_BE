package ERuwatan.Tugasbe.Excell;

import ERuwatan.Tugasbe.model.Kbm;
import ERuwatan.Tugasbe.model.Siswa;
import ERuwatan.Tugasbe.repository.SiswaRepo;
import javassist.NotFoundException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelSiswaSer {

    @Autowired
    private SiswaRepo siswaRepo;

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
            cell4.setCellValue(siswa.getKelas().getNama_kelas());

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
}
