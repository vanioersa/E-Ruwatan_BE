package ERuwatan.Tugasbe.controller;

import ERuwatan.Tugasbe.service.kelasService;
import ERuwatan.Tugasbe.dto.KelasDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/kelas")
@CrossOrigin(origins = "*")
public class kelasController {
    @Autowired
    private kelasService kelasService;

    @GetMapping
    public ResponseEntity<List<KelasDTO>> getAllData() {
        List<KelasDTO> kelasDTOS = kelasService.getAllKelas();
        return new ResponseEntity<>(kelasDTOS, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KelasDTO> getKelasById(@PathVariable Long id) {
        KelasDTO kelasDTO = kelasService.getKelasById(id);
        return ResponseEntity.ok(kelasDTO);
    }

    @PostMapping
    public ResponseEntity<KelasDTO> createKelas(@RequestBody KelasDTO kelasDTO) {
        KelasDTO createdKelas = kelasService.createKelas(kelasDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdKelas);
    }


    @PutMapping("/{id}")
    public ResponseEntity<KelasDTO> updateData(@PathVariable Long id, @RequestBody KelasDTO kelasDTO) {
        KelasDTO updatedKelas = kelasService.updateKelas(id, kelasDTO);
        if (updatedKelas != null) {
            return new ResponseEntity<>(updatedKelas, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKelas(@PathVariable Long id) {
        kelasService.deleteKelas(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}