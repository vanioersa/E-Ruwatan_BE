package ERuwatan.Tugasbe.controller;

import ERuwatan.Tugasbe.Excell.ExcelPiketSer;
import ERuwatan.Tugasbe.dto.PiketDTO;
import ERuwatan.Tugasbe.service.PiketSer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/piket")
public class PiketCont {
    @Autowired
    private PiketSer piketSer;
    @Autowired
    private ExcelPiketSer excelPiketSer;

    @GetMapping("/upload/export-piketan")
    public ResponseEntity<byte[]> exportExcel() {
        return excelPiketSer.exportPiketDataToExcel();
    }

    @GetMapping("/download/template")
    public ResponseEntity<byte[]> downloadPiketTemplate() {
        return excelPiketSer.downloadPiketTemplate();
    }

    @PostMapping("/add")
    public ResponseEntity<String> createPiket(@Valid @RequestBody PiketDTO piketDTO) {
        try {
            PiketDTO createdPiket = piketSer.createPiket(piketDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Piket berhasil dibuat dengan ID: " + createdPiket.getId());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Terjadi kesalahan: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<PiketDTO>> getAllPiket() {
        try {
            List<PiketDTO> piketList = piketSer.getAllPiket();
            return ResponseEntity.ok(piketList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/by-id/{id}")
    public ResponseEntity<?> getPiketById(@PathVariable Long id) {
        try {
            PiketDTO piketDTO = piketSer.getPiketById(id);
            if (piketDTO == null) {
                return new ResponseEntity<>("Piket dengan ID " + id + " tidak ditemukan", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(piketDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Piket dengan ID " + id + " tidak ditemukan", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Terjadi kesalahan: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<String> editPiket(@PathVariable Long id, @Valid @RequestBody PiketDTO piketDTO) {
        try {
            PiketDTO updatedPiket = piketSer.updatePiket(id, piketDTO);
            return ResponseEntity.ok("Piket berhasil diupdate dengan ID: " + updatedPiket.getId());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Terjadi kesalahan: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePiket(@PathVariable Long id) {
        try {
            boolean deleted = piketSer.deletePiket(id);
            if (deleted) {
                return ResponseEntity.ok("Piket dengan ID " + id + " berhasil dihapus.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Piket dengan ID " + id + " tidak ditemukan.");
            }
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Terjadi kesalahan: " + e.getMessage());
        }
    }
}
