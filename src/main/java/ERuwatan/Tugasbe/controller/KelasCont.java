package ERuwatan.Tugasbe.controller;

import ERuwatan.Tugasbe.Excell.ExcelKelasSer;
import ERuwatan.Tugasbe.dto.KelasDTO;
import ERuwatan.Tugasbe.service.KelasSer;
import javassist.NotFoundException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/kelas")
public class KelasCont {
    @Autowired
    private KelasSer kelasSer;

    @Autowired
    private ExcelKelasSer excelKelasSer;

    @PostMapping("/import")
    public void importKelas(@RequestParam("file") MultipartFile file) {
        kelasSer.importKelas(file);
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

//    @GetMapping("/upload/export-siswa")
//    public void exportSiswa(@RequestParam("kelas_id") String kelas,
//                            HttpServletResponse response) throws IOException, NotFoundException {
//        excelKelasSer.excelExportKelas(kelas, response);
//    }
}