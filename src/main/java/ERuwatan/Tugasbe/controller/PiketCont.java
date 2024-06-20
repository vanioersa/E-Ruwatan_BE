package ERuwatan.Tugasbe.controller;

import ERuwatan.Tugasbe.dto.PiketDTO;
import ERuwatan.Tugasbe.dto.SiswaDTO;
import ERuwatan.Tugasbe.service.PiketSer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api/piket")
public class PiketCont {
    @Autowired
    private PiketSer piketSer;

    @PostMapping("/add-siswa")
    public ResponseEntity<String> createPiket(@RequestBody PiketDTO piketDTO) {
        try {
            PiketDTO createdPiket = piketSer.createPiket(piketDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Piket berhasil dibuat dengan ID: " + createdPiket.getId());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Terjadi kesalahan: " + e.getMessage());
        }
    }

    @PutMapping("/ubah/{id}")
    public ResponseEntity<?> updatePiket(@PathVariable Long id, @RequestBody PiketDTO piketDTO) {
        try {
            PiketDTO updatedPiketDTO = piketSer.updatePiket(id, piketDTO);
            return new ResponseEntity<>(updatedPiketDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Piket dengan ID " + id + " tidak ditemukan", HttpStatus.NOT_FOUND);
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

    @GetMapping("/kelas/{kelasId}")
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
