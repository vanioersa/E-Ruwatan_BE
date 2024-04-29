package ERuwatan.Tugasbe.controller;

import ERuwatan.Tugasbe.dto.KelasDTO;
import ERuwatan.Tugasbe.service.KelasSer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.commons.io.IOUtils;

import java.util.List;

@RestController
@RequestMapping("/kelas")
public class KelasCont {
    @Autowired
    private KelasSer kelasSer;

    @PostMapping("/import")
    public void importKelas(@RequestParam("file") MultipartFile file) {
        try {
            // Membaca file Excel
            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);

            // Mengabaikan baris header
            int startRow = 1;

            // Iterasi baris
            for (int i = startRow; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    // Membaca data dari kolom
                    String namaKelas = getStringCellValue(row.getCell(0));
                    String kelas = getStringCellValue(row.getCell(1));

                    // Membuat objek KelasDTO
                    KelasDTO kelasDTO = new KelasDTO();
                    kelasDTO.setNama_kelas(namaKelas);
                    kelasDTO.setKelas(kelas);

                    // Simpan data ke database menggunakan service KelasSer
                    kelasSer.createKelas(kelasDTO);
                }
            }

            // Tutup workbook
            workbook.close();

            // Tampilkan pesan sukses
            System.out.println("Data berhasil diimpor");
        } catch (Exception e) {
            // Tangani exception jika terjadi kesalahan dalam mem-parsing file atau menyimpan data
            e.printStackTrace();
        }
    }

    @PostMapping("/add")
    public KelasDTO createKelas(@RequestBody KelasDTO kelasDTO) {
        return kelasSer.createKelas(kelasDTO);
    }

    @GetMapping("/by-id/{id}")
    public KelasDTO getKelasById(@PathVariable Long id) {
        return kelasSer.getKelasById(id);
    }

    @GetMapping("/all")
    public List<KelasDTO> getAllKelass() {
        return kelasSer.getAllKelas();
    }

    @PutMapping("/ubah/{id}")
    public KelasDTO updateKelas(@PathVariable Long id, @RequestBody KelasDTO kelasDTO) {
        return kelasSer.updateKelas(id, kelasDTO);
    }

    @DeleteMapping("/hapus/{id}")
    public void deleteKelas(@PathVariable Long id) {
        kelasSer.deleteKelas(id);
    }

    private String getStringCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }

        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf((int) cell.getNumericCellValue());
        }

        return null;
    }
}