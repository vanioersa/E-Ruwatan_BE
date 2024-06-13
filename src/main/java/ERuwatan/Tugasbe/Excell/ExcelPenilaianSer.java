package ERuwatan.Tugasbe.Excell;

import ERuwatan.Tugasbe.model.Kbm;
import ERuwatan.Tugasbe.model.Penilaian;
import ERuwatan.Tugasbe.model.Siswa;
import ERuwatan.Tugasbe.model.UserModel;
import ERuwatan.Tugasbe.repository.PenilaianRepo;
import ERuwatan.Tugasbe.repository.SiswaRepo;
import ERuwatan.Tugasbe.repository.UserRepository;
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
import java.util.Optional;

@Service
public class ExcelPenilaianSer {
    @Autowired
    private PenilaianRepo penilaianRepo;

    public void excelExportPenilaian(Long kelas_id, Long siswa_id, HttpServletResponse response) throws IOException, NotFoundException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Export-Penilaian");

        List<Penilaian> penilaianList = penilaianRepo.findByKelasIdAndSiswaId(kelas_id, siswa_id);

        int rowNum = 0;

        Row headerRow = sheet.createRow(rowNum++);
        String[] headers = {"ID", "Nama Siswa", "Kelas", "Nilai Siswa", "Deskripsi"};
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
            cell2.setCellValue(penilaian.getKelas().getNama_kelas());

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
}
