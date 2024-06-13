package ERuwatan.Tugasbe.Excell;

import ERuwatan.Tugasbe.model.Kbm;
import ERuwatan.Tugasbe.model.UserModel;
import ERuwatan.Tugasbe.repository.KbmRepo;
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
public class ExcelKbmSer {

    @Autowired
    private KbmRepo kbmRepo;

    @Autowired
    private UserRepository userRepository;

    private String getNameUser(Long user_id) throws NotFoundException {
        Optional<UserModel> userModel = userRepository.findById(user_id);
        if (userModel.isEmpty()) {
            throw new NotFoundException("User ID tidak ditemukan");
        }
        UserModel user = userModel.get();
        return user.getUsername();
    }

    public void excelExportKbm(Long kelas_id, Long user_id, HttpServletResponse response) throws IOException, NotFoundException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Export-Kbm");

        List<Kbm> kbmList = kbmRepo.findByKelasIdAndUserId(kelas_id, user_id);

        int rowNum = 0;

        Row headerRow = sheet.createRow(rowNum++);
        String[] headers = {"ID", "Nama Guru", "Kelas", "Jam Masuk", "Jam Pulang", "Keterangan", "Materi"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        for (Kbm kbm : kbmList) {
            Row row = sheet.createRow(rowNum++);
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(kbm.getId());

            Cell cell1 = row.createCell(1);
            cell1.setCellValue(getNameUser(kbm.getUserModel().getId()));

            Cell cell2 = row.createCell(2);
            cell2.setCellValue(kbm.getKelas().getNama_kelas());

            Cell cell3 = row.createCell(3);
            cell3.setCellValue(kbm.getJam_masuk());

            Cell cell4 = row.createCell(4);
            cell4.setCellValue(kbm.getJam_pulang());

            Cell cell5 = row.createCell(5);
            cell5.setCellValue(kbm.getKeterangan());

            Cell cell6 = row.createCell(6);
            cell6.setCellValue(kbm.getMateri());
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=ExportKBM.xlsx");
        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
