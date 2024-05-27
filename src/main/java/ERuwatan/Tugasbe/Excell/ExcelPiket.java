package ERuwatan.Tugasbe.Excell;

import ERuwatan.Tugasbe.dto.KelasDTO;
import ERuwatan.Tugasbe.dto.SiswaDTO;
import ERuwatan.Tugasbe.model.Piket;
import ERuwatan.Tugasbe.model.Siswa;
import ERuwatan.Tugasbe.repository.KelasRepo;
import ERuwatan.Tugasbe.repository.SiswaRepo;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class ExcelPiket {
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[] HEADERs = {"tanggal", "status", "kelas", "siswa"};

    static String SHEET = "Sheet1";

    @Autowired
    private static KelasRepo kelasRepo;

    public ExcelPiket(KelasRepo kelasRepo) {
        this.kelasRepo = kelasRepo;
    }

    private static SiswaRepo siswaRepo;

    public ExcelPiket(SiswaRepo siswaRepo1) {
        this.siswaRepo = siswaRepo1;
    }

    public static boolean hasExcelFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }


    public static ByteArrayInputStream pasienToExcel(List<Piket> pasiens) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet(SHEET);

            Row headerRow = sheet.createRow(0);

            for (int col = 0; col < HEADERs.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADERs[col]);
            }


            int rowIdx = 1;
            for (Piket pasien : pasiens) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(1).setCellValue(pasien.getTanggal());
                row.createCell(2).setCellValue(pasien.getStatus());
                row.createCell(3).setCellValue(pasien.getKelasId().getNama_kelas());
                row.createCell(4).setCellValue(pasien.getSiswaId().getNama_siswa());
            }
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch(IOException e){
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        }
    }
    public static List<Piket> excelPiket(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();

            List<Piket> piketList = new ArrayList<>();
            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();
                Piket piket = new Piket();
                int cellIdx = 0;

                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellIdx) {
                        case 1:
                            piket.setTanggal(currentCell.getStringCellValue());
                            break;
                        case 2:
                            piket.setStatus(currentCell.getStringCellValue());
                            break;
                        case 3:
                            String kelasName = currentCell.getStringCellValue();
                            KelasDTO kelasDTO = kelasRepo.findByNamaKelas(kelasName);
                            piket.setKelasId(kelasDTO);
                            break;
                        case 4:
                            String siswaName = currentCell.getStringCellValue();
                            SiswaDTO siswaDTO = siswaRepo.findBySiswa(siswaName);
                            piket.setSiswaId(siswaDTO);
                            break;
                        default:
                            break;
                    }
                    cellIdx++;
                }
                piketList.add(piket);
            }
            workbook.close();
            return piketList;
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse Excel file: " + e.getMessage());
        }
    }

}
