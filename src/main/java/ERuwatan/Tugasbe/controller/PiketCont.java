package ERuwatan.Tugasbe.controller;

import ERuwatan.Tugasbe.Excell.ExcelPiketSer;
import ERuwatan.Tugasbe.PDF.PdfPiketSer;
import ERuwatan.Tugasbe.dto.PiketDTO;
import ERuwatan.Tugasbe.service.PiketSer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/piket")
public class PiketCont {
    @Autowired
    private PiketSer piketSer;
    @Autowired
    private ExcelPiketSer excelPiketSer;
    @Autowired
    private PdfPiketSer pdfPiketSer;

    private static final Logger LOGGER = Logger.getLogger(PiketCont.class.getName());

    @GetMapping("/export-piketan-pdf")
    public ResponseEntity<byte[]> exportPiketDataToPdf() {
        return pdfPiketSer.exportPiketDataToPdf();
    }

    @GetMapping("/export-by-id/pdf/{id}")
    public ResponseEntity<byte[]> exportPiketByIdToPdf(@PathVariable Long id) {
        return pdfPiketSer.exportPiketByIdToPdf(id);
    }

    @GetMapping("/upload/export-piketan")
    public ResponseEntity<byte[]> exportExcel() {
        return excelPiketSer.exportPiketDataToExcel();
    }

    @PostMapping("/upload/import-piketan")
    public ResponseEntity<Map<String, Object>> importPiketFromExcel(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        if (file.isEmpty()) {
            response.put("message", "File is empty");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            List<String> messages = excelPiketSer.importPiketFromExcel(file);
            if (messages.isEmpty()) {
                response.put("message", "File imported successfully");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("messages", messages);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to import file", e);
            response.put("message", "Failed to import file");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
