package ERuwatan.Tugasbe.controller;

import ERuwatan.Tugasbe.dto.SiswaDTO;
import ERuwatan.Tugasbe.model.SiswaModel;
import ERuwatan.Tugasbe.service.SiswaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping("/siswa")
public class SiswaController {
    @Autowired
    private SiswaService siswaService;

    @GetMapping("/all")
    public ResponseEntity<List<SiswaDTO>> getAllSiswa() {
        List<SiswaDTO> siswaDTOs = siswaService.getAllSiswa();
        return new ResponseEntity<>(siswaDTOs, HttpStatus.OK);
    }

    @GetMapping("/by-id/{id}")
    public ResponseEntity<SiswaDTO> getSiswaById(@PathVariable Long id) {
        SiswaDTO siswaDTO = siswaService.getSiswaById(id);
        return ResponseEntity.ok(siswaDTO);
    }

    @GetMapping("/by-kelas-id/{kelasId}")
    public ResponseEntity<List<SiswaModel>> getSiswaByKelasId (@PathVariable Long kelasId){
        List<SiswaModel> siswaModel = siswaService.getSiswaByKelasId(kelasId);
        return ResponseEntity.ok(siswaModel);
    }

    @PostMapping("/add")
    public ResponseEntity<SiswaDTO> createSiswa(@RequestBody SiswaDTO siswaRequestDTO) {
        SiswaDTO createdSiswa = siswaService.createSiswa(siswaRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSiswa);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SiswaDTO> updateSiswa(@PathVariable Long id, @RequestBody SiswaDTO siswaRequestDTO) {
        SiswaDTO updatedSiswa = siswaService.updateSiswa(id, siswaRequestDTO);
        if (updatedSiswa != null) {
            return new ResponseEntity<>(updatedSiswa, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSiswa(@PathVariable Long id) {
        siswaService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
