package ERuwatan.Tugasbe.controller;

import ERuwatan.Tugasbe.dto.GuruDTO;
import ERuwatan.Tugasbe.dto.SiswaDTO;
import ERuwatan.Tugasbe.model.GuruModel;
import ERuwatan.Tugasbe.service.GuruService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/guru")
public class GuruController {
    @Autowired
    private GuruService guruService;

    @GetMapping("/all")
    public ResponseEntity<List<GuruDTO>> getAllGuru() {
        List<GuruDTO> guruDTOS = guruService.getAllGuru();
        return new ResponseEntity<>(guruDTOS, HttpStatus.OK);
    }

    @GetMapping("/by-id/{id}")
    public ResponseEntity<GuruDTO> getGuruById(@PathVariable Long id) {
        GuruDTO guruDTO = guruService.getGuruById(id);
        return ResponseEntity.ok(guruDTO);
    }

    @PostMapping("/add")
    public ResponseEntity<GuruDTO> createSiswa(@RequestBody GuruDTO guruDTO) {
        GuruDTO createdGuru = guruService.createGuru(guruDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGuru);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GuruDTO> updateguru(@PathVariable Long id, @RequestBody GuruDTO guruDTO) {
        GuruDTO updatedGuru = guruService.updateGuru(id, guruDTO);
        if (updatedGuru != null) {
            return new ResponseEntity<>(updatedGuru, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGuru(@PathVariable Long id) {
        guruService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
