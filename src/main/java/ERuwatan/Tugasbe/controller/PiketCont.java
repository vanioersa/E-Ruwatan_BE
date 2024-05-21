package ERuwatan.Tugasbe.controller;

import ERuwatan.Tugasbe.Excell.ExcelImportService;
import ERuwatan.Tugasbe.dto.PiketDTO;
import ERuwatan.Tugasbe.service.PiketSer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/piket")
public class PiketCont {
    @Autowired
    private PiketSer piketSer;

    @PostMapping("/add")
    public PiketDTO createPiket(@RequestBody PiketDTO piketDTO) {
        return piketSer.createPiket(piketDTO);
    }

    @GetMapping("/by-id/{id}")
    public PiketDTO getPiketById(@PathVariable Long id) {
        return piketSer.getPiketById(id);
    }

    @GetMapping("/all")
    public List<PiketDTO> getAllPikets() {
        return piketSer.getAllPikets();
    }

    @PutMapping("/ubah/{id}")
    public PiketDTO updatePiket(@PathVariable Long id, @RequestBody PiketDTO piketDTO) {
        return piketSer.updatePiket(id, piketDTO);
    }

    @DeleteMapping("/hapus/{id}")
    public void deletePiket(@PathVariable Long id) {
        piketSer.deletePiket(id);
    }

    @PostMapping("/import")
    public ResponseEntity<String> importData(@RequestParam("file") MultipartFile file) throws IOException {
        ExcelImportService excelImportService = new ExcelImportService();
        excelImportService.importDataFromExcel(file);
        return ResponseEntity.ok("Data imported successfully!");
    }

}