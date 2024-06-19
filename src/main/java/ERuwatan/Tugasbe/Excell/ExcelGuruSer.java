package ERuwatan.Tugasbe.Excell;

import ERuwatan.Tugasbe.model.Guru;
import ERuwatan.Tugasbe.model.Siswa;
import ERuwatan.Tugasbe.model.UserModel;
import ERuwatan.Tugasbe.repository.GuruRepo;
import ERuwatan.Tugasbe.repository.UserRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelGuruSer {

    @Autowired
    private GuruRepo guruRepo;

    @Autowired
    private UserRepository userRepository;

    public void excelExportGuru(HttpServletResponse response) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Export-Guru");

        List<UserModel> userModelList = userRepository.findAll(); // Adjust this according to your repository method

        int rowNum = 0;

        Row headerRow = sheet.createRow(rowNum++);
        String[] headers = {"ID", "Nama Guru", "Email", "Jenis Kelamin", "Alamat", "Nomor Telepon", "Status Pernikahan"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        for (UserModel guru : userModelList) {
            Row row = sheet.createRow(rowNum++);
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(guru.getId());

            Cell cell1 = row.createCell(1);
            cell1.setCellValue(guru.getUsername());

            Cell cell2 = row.createCell(2);
            cell2.setCellValue(guru.getEmail());

            Cell cell3 = row.createCell(3);
            cell3.setCellValue(guru.getGender());

            Cell cell4 = row.createCell(4);
            cell4.setCellValue(guru.getAlamat());

            Cell cell5 = row.createCell(5);
            cell5.setCellValue(guru.getTelepon());

            Cell cell6 = row.createCell(6);
            cell6.setCellValue(guru.getStatus_nikah());
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=ExportGuru.xlsx");
        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
