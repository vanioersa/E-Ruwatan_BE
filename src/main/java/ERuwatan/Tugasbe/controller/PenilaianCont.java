
package ERuwatan.Tugasbe.controller;

import ERuwatan.Tugasbe.Excell.ExcelPenilaianSer;
import ERuwatan.Tugasbe.dto.PenilaianDTO;
import ERuwatan.Tugasbe.model.Penilaian;
import ERuwatan.Tugasbe.serlmpl.PenilaianSerImpl;
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
@RequestMapping("/panilaian")
@CrossOrigin(origins = "*")
public class PenilaianCont {
    @Autowired
    private PenilaianSerImpl penilaianSer;

    @Autowired
    private ExcelPenilaianSer excelPenilaianSer;

    @PostMapping("/add")
    private PenilaianDTO createPenilaian (@RequestBody PenilaianDTO penilaianDTO) {
        return penilaianSer.createPenilaian(penilaianDTO);
    }

    @GetMapping("/by-id/{id}")
    public PenilaianDTO getPenilaianById(@PathVariable Long id) {
        return penilaianSer.getPenilaianById(id);
    }

    @GetMapping("/all")
    public List<Penilaian> getAllPenilaian() {
        return penilaianSer.getAllPenilaian();
    }

    @PutMapping("/ubah/{id}")
    public Penilaian updatePenilaian(@PathVariable Long id, @RequestBody PenilaianDTO penilaianDTO) {
        return penilaianSer.updatePenilaian(id, penilaianDTO);
    }

    @DeleteMapping("/hapus/{id}")
    public void deletePenilaian(@PathVariable Long id) {
        penilaianSer.deletePenilaian(id);
    }

    @PostMapping("/upload/importPenilaian")
    public ResponseEntity<String> importSiswaFromExcel(@RequestPart("file") MultipartFile file) {
        try {
            excelPenilaianSer.importPenilaianFromExcel(file);
            return new ResponseEntity<>("Import successful", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Import failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Import failed: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/upload/export-penilaian")
    public void ExportPenilaian(@RequestParam("kelas_id") Long kelas_id,
                          @RequestParam("siswa_id") Long siswa_id,
                          HttpServletResponse response) throws IOException, NotFoundException {
        excelPenilaianSer.excelExportPenilaian(kelas_id, siswa_id, response);
    }
}
