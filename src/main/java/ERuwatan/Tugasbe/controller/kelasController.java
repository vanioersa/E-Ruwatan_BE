package ERuwatan.Tugasbe.controller;

import ERuwatan.Tugasbe.model.KelasModel;
import ERuwatan.Tugasbe.service.kelasService;
import ERuwatan.Tugasbe.dto.KelasDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/kelas")
@CrossOrigin // Menambahkan dukungan CORS (sesuaikan nilai jika perlu)
public class kelasController {
    @Autowired
    private kelasService kelasService;

    @GetMapping("/all")
    public ResponseEntity<List<KelasModel>> getAllData() {
        List<KelasModel> kelasDTOS = kelasService.getAllKelas();
        return ResponseEntity.ok(kelasDTOS);
    }

    @GetMapping("/by-id/{id}")
    public ResponseEntity<KelasModel> getKelasById(@PathVariable Long id) {
        try {
            KelasModel kelasDTO = kelasService.getKelasById(id);
            return ResponseEntity.ok(kelasDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<KelasModel> createKelas(@RequestBody KelasModel kelasModel) {
        KelasModel createdKelas = kelasService.createKelas(kelasModel);
        return new ResponseEntity<>(createdKelas, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<KelasModel> updateKelas(@PathVariable Long id, @RequestBody KelasDTO kelasRequestDTO) {
        try {
            KelasModel updatedKelas = kelasService.updateKelas(id, kelasRequestDTO);
            return new ResponseEntity<>(updatedKelas, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/hapus/{id}")
    public ResponseEntity<Void> deleteKelas(@PathVariable Long id) {
        try {
            kelasService.deleteKelas(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}