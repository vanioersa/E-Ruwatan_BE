package ERuwatan.Tugasbe.controller;

import ERuwatan.Tugasbe.Excell.ExcelSiswa;
import ERuwatan.Tugasbe.Excell.ExcelSiswaSer;
import ERuwatan.Tugasbe.dto.SiswaDTO;
import ERuwatan.Tugasbe.model.Siswa;
import ERuwatan.Tugasbe.response.ResponseMessage;
import ERuwatan.Tugasbe.service.SiswaSer;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/siswa")
public class SiswaCont {
    @Autowired
    private SiswaSer siswaSer;

    @Autowired
    private ExcelSiswaSer excelSiswaSer;

    @GetMapping("/by-kelas-id/{kelasId}")
    public ResponseEntity<List<Siswa>> getSiswaByKelasId(@PathVariable Long kelasId) {
        List<Siswa> siswa = siswaSer.getSiswaByKelasId(kelasId);
        return ResponseEntity.ok(siswa);
    }

    @PostMapping("/add")
    public SiswaDTO createMurid(@RequestBody SiswaDTO muridDTO) {
        return siswaSer.createSiswa(muridDTO);
    }

    @GetMapping("/by-id/{id}")
    public SiswaDTO getMuridById(@PathVariable Long id) {
        return siswaSer.getSiswaById(id);
    }

    @GetMapping("/all")
    public List<SiswaDTO> getAllMurids() {
        return siswaSer.getAllSiswas();
    }

    @PutMapping("/ubah/{id}")
    public SiswaDTO updateMurid(@PathVariable Long id, @RequestBody SiswaDTO siswaDTO) {
        return siswaSer.updateSiswa(id, siswaDTO);
    }

    @DeleteMapping("/hapus/{id}")
    public void deleteMurid(@PathVariable Long id) {
        siswaSer.deleteSiswa(id);
    }

    @GetMapping("/upload/export-siswa")
    public void ExportSiswa(@RequestParam("kelas_id") Long kelas_id,
                          HttpServletResponse response) throws IOException, NotFoundException {
        excelSiswaSer.excelExportSiswa(kelas_id, response);
    }

//    @PostMapping(path = "/upload/importSiswa")
//    public ResponseEntity<ResponseMessage> uploadFile(@RequestPart("file") MultipartFile file) {
//        String message = "";
//        if (ExcelSiswa.hasExcelFormat(file)) {
//            try {
//                siswaSer.saveSiswa(file);
//                message = "Uploaded the file successfully: " + file.getOriginalFilename();
//                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
//            } catch (Exception e) {
//                System.out.println(e);
//                message = "Could not upload the file: " + file.getOriginalFilename() + "!";
//                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
//            }
//        }
//        message = "Please upload an excel file!";
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
//    }

    @PostMapping("/upload/import-siswa")
    public ResponseEntity<String> importSiswaFromExcel(@RequestPart("file") MultipartFile file) {
        try {
            excelSiswaSer.importSiswaFromExcel(file);
            return new ResponseEntity<>("Import successful", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Import failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Import failed: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}