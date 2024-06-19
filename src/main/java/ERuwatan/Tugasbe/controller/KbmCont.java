package ERuwatan.Tugasbe.controller;

import ERuwatan.Tugasbe.Excell.ExcelKbm;
import ERuwatan.Tugasbe.Excell.ExcelKbmSer;
import ERuwatan.Tugasbe.Excell.ExcelPiket;
import ERuwatan.Tugasbe.dto.KbmDTO;
import ERuwatan.Tugasbe.response.ResponseMessage;
import ERuwatan.Tugasbe.service.KbmSer;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/kbm")
public class KbmCont {

    @Autowired
    private KbmSer kbmSer;

    @Autowired
    private ExcelKbmSer excelKbmSer;

    @PostMapping("/add")
    public KbmDTO createKbm(@RequestBody KbmDTO kbmDTO) {
        return kbmSer.createKbm(kbmDTO);
    }

    @GetMapping("/by-id/{id}")
    public KbmDTO getKbmById(@PathVariable Long id) {
        return kbmSer.getKbmById(id);
    }

    @GetMapping("/all")
    public List<KbmDTO> getAllKbm() {
        return kbmSer.getAllKbm();
    }

    @PutMapping("/ubah/{id}")
    public KbmDTO updateKbm(@PathVariable Long id, @RequestBody KbmDTO kbmDTO) {
        return kbmSer.updateKbm(id, kbmDTO);
    }

    @DeleteMapping("/hapus/{id}")
    public void deletKbm(@PathVariable Long id) {
        kbmSer.deleteKbm(id);
    }

    @GetMapping("/upload/export-kbm")
    public void ExportKbm(@RequestParam("kelas_id") Long kelas_id,
                            @RequestParam("user_id") Long user_id,
                            HttpServletResponse response) throws IOException, NotFoundException {
        excelKbmSer.excelExportKbm(kelas_id, user_id, response);
    }

    @PostMapping("/upload/import-KBM")
    public ResponseEntity<String> importSiswaFromExcel(@RequestPart("file") MultipartFile file, @RequestParam("userId") Long userId) {
        try {
            excelKbmSer.importKBMFromExcel(file, userId);
            return new ResponseEntity<>("Import successful", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Import failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Import failed: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            return new ResponseEntity<>("Import failed: " + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}