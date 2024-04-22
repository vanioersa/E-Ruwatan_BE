package ERuwatan.Tugasbe.controller;

import ERuwatan.Tugasbe.dto.SiswaDTO;
import ERuwatan.Tugasbe.dto.kelasDTO;
import ERuwatan.Tugasbe.service.SiswaService;
import ERuwatan.Tugasbe.service.kelasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(name = "/siswa")
@CrossOrigin(origins = "*")
public class SiswaController {
    @Autowired
    private SiswaService siswaService;

    @GetMapping
    public ResponseEntity<List<SiswaDTO>> getAllData() {
        List<SiswaDTO> siswaDTOS = siswaService.getAllSiswa();
        return new ResponseEntity<>(siswaDTOS, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SiswaDTO> createData(@RequestBody SiswaDTO siswaDTO) {
        SiswaDTO newData = siswaService.createSiswa(siswaDTO);
        return new ResponseEntity<>(newData, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        siswaService.deleteSiswa(id);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
