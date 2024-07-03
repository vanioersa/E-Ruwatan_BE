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

    @GetMapping("/export-excel")
    public ResponseEntity<byte[]> exportExcel() {
        return excelPiketSer.exportPiketDataToExcel();
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

    @PutMapping("/ubah/{id}")
    public ResponseEntity<?> updatePiket(@PathVariable Long id, @Valid @RequestBody PiketDTO piketDTO) {
        try {
            PiketDTO updatedPiketDTO = piketSer.updatePiket(id, piketDTO);
            return new ResponseEntity<>(updatedPiketDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Piket dengan ID " + id + " tidak ditemukan", HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Terjadi kesalahan: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/hapus-tanggal")
    public ResponseEntity<String> deletePiketByDateAndClass(
            @RequestParam String tanggal, @RequestParam Long kelasId) {
        try {
            boolean deleted = piketSer.deletePiketByDateAndClass(tanggal, kelasId);
            if (deleted) {
                return ResponseEntity.ok("Piket berhasil dihapus untuk tanggal " + tanggal + " dan kelas dengan ID " + kelasId);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tidak ada piket yang ditemukan untuk tanggal dan kelas tersebut.");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Terjadi kesalahan: " + e.getMessage());
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

    @GetMapping("/by-kelas/{kelasId}")
    public ResponseEntity<List<PiketDTO>> getPiketByKelas(@PathVariable Long kelasId) {
        try {
            List<PiketDTO> piketList = piketSer.getPiketByKelas(kelasId);
            if (piketList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(piketList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}