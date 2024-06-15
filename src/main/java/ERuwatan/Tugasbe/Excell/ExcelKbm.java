package ERuwatan.Tugasbe.Excell;

import ERuwatan.Tugasbe.dto.KelasDTO;
import ERuwatan.Tugasbe.model.Kbm;
import ERuwatan.Tugasbe.model.UserModel;
import ERuwatan.Tugasbe.repository.KelasRepo;
import ERuwatan.Tugasbe.repository.UserRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class ExcelKbm {

//    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
//    static String[] HEADERS_KBM = {"ID","Nama Guru", "Kelas", "Jam Masuk", "Jam Pulang", "Keterangan", "Materi"};
//    static String SHEET = "Sheet1";
//
//    private KelasRepo kelasRepo;
//    private UserRepository userRepository;
//
//    @Autowired
//    public ExcelKbm(KelasRepo kelasRepo, UserRepository userRepository) {
//        this.kelasRepo = kelasRepo;
//        this.userRepository = userRepository;
//    }
//
//    public static boolean hasExcelFormat(MultipartFile file) {
//        return TYPE.equals(file.getContentType());
//    }
//
//    public ByteArrayInputStream kbmToExcel(List<Kbm> kbmList) throws IOException {
//        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
//            Sheet sheet = workbook.createSheet(SHEET);
//            Row headerRow = sheet.createRow(0);
//
//            for (int col = 0; col < HEADERS_KBM.length; col++) {
//                Cell cell = headerRow.createCell(col);
//                cell.setCellValue(HEADERS_KBM[col]);
//            }
//
//            int rowIdx = 1;
//            for (Kbm kbm : kbmList) {
//                Row row = sheet.createRow(rowIdx++);
//                row.createCell(0).setCellValue(kbm.getId());
//                row.createCell(1).setCellValue(kbm.getUserModel().getUsername());
//                row.createCell(2).setCellValue(kbm.getKelas().getNama_kelas());
//                row.createCell(3).setCellValue(kbm.getJam_masuk());
//                row.createCell(4).setCellValue(kbm.getJam_pulang());
//                row.createCell(5).setCellValue(kbm.getKeterangan());
//                row.createCell(6).setCellValue(kbm.getMateri());
//            }
//
//            workbook.write(out);
//            return new ByteArrayInputStream(out.toByteArray());
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to export data to Excel file: " + e.getMessage());
//        }
//    }
//
//    public List<Kbm> excelToKbm(InputStream is) {
//        try {
//            Workbook workbook = new XSSFWorkbook(is);
//            Sheet sheet = workbook.getSheet(SHEET);
//            Iterator<Row> rows = sheet.iterator();
//
//            List<Kbm> kbmList = new ArrayList<>();
//            int rowNumber = 0;
//            while (rows.hasNext()) {
//                Row currentRow = rows.next();
//
//                if (rowNumber == 0) {
//                    rowNumber++;
//                    continue;
//                }
//
//                Iterator<Cell> cellsInRow = currentRow.iterator();
//                Kbm kbm = new Kbm();
//                int cellIdx = 0;
//
//                while (cellsInRow.hasNext()) {
//                    Cell currentCell = cellsInRow.next();
//
//                    switch (cellIdx) {
//                        case 0:
//                            kbm.setId((long) currentCell.getNumericCellValue());
//                            break;
//                        case 1:
//                            String username = currentCell.getStringCellValue();
//                            Optional<UserModel> optionalUser = userRepository.findById(username);
//                            if (optionalUser.isPresent()) {
//                                UserModel user = optionalUser.get();
//                                kbm.setUserModel(user);
//                            } else {
//                                throw new EntityNotFoundException("User not found: " + username);
//                            }
//                            break;
//                        case 2:
//                            String kelasName = currentCell.getStringCellValue();
//                            Optional<KelasDTO> optionalKelasDTO = kelasRepo.findByNamaKelas(kelasName);
//                            if (optionalKelasDTO.isPresent()) {
//                                KelasDTO kelasDTO = optionalKelasDTO.get();
//                                // Ambil kelas berdasarkan ID dari optionalKelasDTO, bukan langsung dari kbm.getKelas()
//                                kbm.setKelas(kelasRepo.getOne(kelasDTO.getId()));
//                            } else {
//                                throw new EntityNotFoundException("Kelas not found: " + kelasName);
//                            }
//                            break;
//                        case 3:
//                            kbm.setJam_masuk(currentCell.getStringCellValue());
//                            break;
//                        case 4:
//                            kbm.setJam_pulang(currentCell.getStringCellValue());
//                            break;
//                        case 5:
//                            kbm.setKeterangan(currentCell.getStringCellValue());
//                            break;
//                        case 6:
//                            kbm.setMateri(currentCell.getStringCellValue());
//                            break;
//                        default:
//                            break;
//                    }
//                    cellIdx++;
//                }
//
//                kbmList.add(kbm);
//            }
//
//            workbook.close();
//            return kbmList;
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to parse Excel file: " + e.getMessage());
//        }
//    }

}
